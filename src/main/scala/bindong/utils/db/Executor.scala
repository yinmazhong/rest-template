package bindong.utils.db

import java.sql.{Connection, ResultSet, SQLException, Statement}

import bindong.common.log.LogSupportProj

/**
  * Created by xubanxian on 17-6-20.
  * more comments,less complains.
  */
object Executor {
  trait QueryExecutor {
    this: Sql =>

    //for transaction
    def query(conn: Connection): SqlResult = DbAccessor.query(conn, sqlStatement)

    //for standalone
    def query(implicit connFactory: ConnectionFactory): SqlResult = {
      val conn = connFactory.createConnection
      if(conn == null)
      {
        return SqlResult(List(), List())
      }
      try {
        query(conn)
      } finally {
        if (conn != null) conn.close()
      }

    }
  }

  trait QueryIterator {
    this: Sql =>
    //for transaction
    def queryByIterator(conn: Connection): SqlIterator = DbAccessor.queryByIterator(conn, sqlStatement)

    //for standalone
    def queryByIterator(implicit connFactory: ConnectionFactory): SqlIterator = {
      val conn = connFactory.createConnection
      try {
        queryByIterator(conn)
      }
    }
  }

  trait CommandExecutor {
    this: Sql =>

    //for transaction
    def execute(conn: Connection): Boolean = DbAccessor.execute(conn, sqlStatement)

    //for standalone
    def execute(implicit connFactory: ConnectionFactory): Boolean = {
      val conn = connFactory.createConnection
      try {
        execute(conn)
      } finally {
        if (conn != null) conn.close()
      }
    }

    //for transaction
    def update(conn: Connection): Boolean = DbAccessor.update(conn, sqlStatement)

    //for standalone
    def update(implicit connFactory: ConnectionFactory): Boolean = {
      val conn = connFactory.createConnection
      try {
        update(conn)
      } finally {
        if (conn != null) conn.close()
      }
    }
  }

  trait CommandListExecutor {
    this: SqlList =>

    def query(implicit connFactory: ConnectionFactory): List[SqlResult] = {
      val conn = connFactory.createConnection
      try {
        sqlStatements.map(x => DbAccessor.query(conn, x))
      } finally {
        if (conn != null) conn.close()
      }
    }

    def executeBatch(implicit connFactory: ConnectionFactory): Boolean = {
      val conn = connFactory.createConnection
      try {
        DbAccessor.executeBatch(conn, sqlStatements)
      } finally {
        if (conn != null) conn.close()
      }
    }

    def executeBatchWithLock(implicit connFactory: ConnectionFactory): Boolean = {
      val conn = connFactory.createConnection
      try {
        sqlStatements.map(x => DbAccessor.execute(conn, x)).reduce(_ && _)
      } finally {
        if (conn != null) conn.close()
      }
    }
  }

  object DbAccessor extends LogSupportProj {
    def query(conn: Connection, sql: String): SqlResult = {
      var stmt: Statement = null
      var rs: ResultSet = null
      try {
        stmt = conn.createStatement()
        logger.debug(s"db query: $sql")
        rs = stmt.executeQuery(sql)
        SqlResult(rs.columns, rs.rows)
      }
      catch {
        case e: SQLException => {
          logger.error("catch SQLException when executeQuery sql: " + sql, e)
          logger.error("e.getCause:" + e.getCause)
          SqlResult(List(), List())
        }
        case ex:Exception => {
          logger.error("catch exception:"+ ex)
          logger.error("ex.getCause:" + ex.getCause)
          SqlResult(List(), List())
        }
      }
      finally {
        try {
          if (rs != null) rs.close()
          if (stmt != null) stmt.close()
        }
        catch {
          case e: SQLException => logger.error("catch SQLException in rs.close or stmt.close or conn.close()!")
        }
      }
    }

    def queryByIterator(conn: Connection, sql: String): SqlIterator = {
      var stmt: Statement = null
      var rs: ResultSet = null
      try {
        stmt = conn.createStatement()
        logger.debug(s"db query by iterator: $sql")
        rs = stmt.executeQuery(sql)
        SqlIterator(conn, stmt, rs)
      }
      catch {
        case e: SQLException => logger.error("catch SQLException when get sql iterator: " + sql, e)
          try {
            if (rs != null) rs.close()
            if (stmt != null) stmt.close()
            if (conn != null) conn.close()
          } catch {
            case e: SQLException => logger.error("catch SQLException in rs.close or stmt.close or conn.close()!")
          }
          null
      }
      finally {
      }
    }

    def update(conn: Connection, sql: String): Boolean = {
      var stmt: Statement = null
      try {
        stmt = conn.createStatement()
        logger.debug(s"db update: $sql")
        stmt.executeUpdate(sql)
        true
      }
      catch {
        case e: SQLException => logger.error("catch SQLException when executeUpdate sql: " + sql, e)
          false
      }
      finally {
        try {
          if (stmt != null) stmt.close()
        }
        catch {
          case e: SQLException => logger.error("catch SQLException in stmt.close or conn.close()!")
        }
      }
    }

    def execute(conn: Connection, sql: String): Boolean = {
      var stmt: Statement = null
      try {
        stmt = conn.createStatement()
        logger.debug(s"db execute: $sql")
        stmt.execute(sql)
        true
      }
      catch {
        case e: SQLException => logger.error("catch SQLException when execute sql: " + sql, e)
          false
      }
      finally {
        try {
          if (stmt != null) stmt.close()
        }
        catch {
          case e: SQLException => logger.error("catch SQLException in stmt.close or conn.close()!")
        }
      }
    }

    def executeBatch(conn: Connection, sqls: List[String]): Boolean = {
      var stmt: Statement = null
      try {
        stmt = conn.createStatement()
        logger.info(s"db executeBatch: $sqls")
        sqls.foreach(stmt.addBatch)
        stmt.executeBatch()
        true
      }
      catch {
        case e: SQLException => logger.error(s"catch SQLException when execute batch sql: $sqls", e)
          false
      }
      finally {
        try {
          if (stmt != null) stmt.close()
        }
        catch {
          case e: SQLException => logger.error("catch SQLException in stmt.close or conn.close()!", e)
        }
      }
    }
  }
}
