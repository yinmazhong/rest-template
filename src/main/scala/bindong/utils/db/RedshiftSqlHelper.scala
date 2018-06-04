package bindong.utils.db

import java.sql.{Connection, ResultSet, SQLException, Statement}

/**
  * Created by xubanxian on 17-6-16.
  * more comments,less complains.
  */
class RedshiftSqlHelper(sql:String) extends UsingConnectionF with UsingStatementF{
  implicit def factory = new RedShiftConnectionFactory {}

  def connection = factory.createConnection

  /*def query():List[List[String]] = {
    UsingConnectionFLL { connection =>
      getResult(getStatement(connection).get.executeQuery(sql))
    }
  }*/
  def query():Option[SqlResult]= {
    UsingConnectionF{ connection =>
      Sql(sql).query(connection)
      //getStatement(connection).get.executeQuery(sql)
    }
  }

  def execute():Boolean = {
    UsingConnectionF[Boolean] { connection =>
      Sql(sql).execute(connection)
    } match {
      case Some(true) => true
      case Some(false) => false
      case _ => {
        logger.error(s"execute sql: $sql failure")
        false
      }
    }
  }

  protected def getStatement(connection: Connection): Option[Statement] = {
    if (connection == null) logger.error("[getStatement] connection id null")
    try {
      Some(connection.createStatement())
    } catch {
      case ex: SQLException => {
        logger.error("createStatement failure")
        None
      }
      case _ => {
        logger.error("[createStatement] unkown error")
        None
      }
    }finally {

    }
  }
  protected def getResult(resultSet:ResultSet): List[List[String]] ={
    var res = List(List(""))
      while(resultSet.next()){
        res.++((0 to resultSet.getMetaData.getColumnCount).toList.map(resultSet.getNString(_)))
      }
    res.tail
  }
}
//case class RedshiftSqlHelper(sql:String) extends SqlHelper(sql) with RedShiftConnectionFactory
