package bindong.utils.db

import java.sql.{Connection, ResultSet, SQLException, Statement}

import bindong.common.log.LogSupportProj


/**
  * Created by xubanxian on 17-6-20.
  * more comments,less complains.
  */
class SqlIterator(conn: Connection, stmt: Statement, iterator: ResultSet) extends LogSupportProj {
  var closed = false
  var totalRowNum = 0

  def close(): Unit = {
    if (!closed) {
      try {
        if (iterator != null) iterator.close()
        if (stmt != null) stmt.close()
        if (conn != null) conn.close()
        logger.debug(s"close iterator successfully.")
      } catch {
        case e: SQLException => logger.error("catch SQLException in close iterator!")
      } finally {
        closed = true
      }
    }
  }

  private def hasNext: Boolean = {
    closed match {
      case true => false
      case false => iterator.next() match {
        case true => true
        case false => close()
          false
      }
    }
  }

  def next(n: Int): SqlResult = {
    var counter = 0
    var valueList: List[List[String]] = List()
    while (hasNext) {
      if (counter < n) {
        counter = counter + 1
        val oneLine = (1 to columnCount).map(iterator.getString).toList
        totalRowNum += 1
        valueList = oneLine :: valueList
      } else {
        return SqlResult(columns, valueList.reverse)
      }

      // when counter == n should return, otherwise the while (hasNext) will jump to the next row.
      if (counter == n) {
        return SqlResult(columns, valueList.reverse)
      }
    }
    SqlResult(columns, valueList.reverse)
  }

  private val columnCount = if (iterator != null) {
    iterator.getMetaData.getColumnCount
  } else {
    close()
    0
  }

  val columns: List[String] = {
    if (iterator != null) {
      (1 to columnCount).map(iterator.getMetaData.getColumnName).toList
    } else {
      List()
    }
  }
}

object SqlIterator {
  def apply(conn: Connection, stmt: Statement, resultSet: ResultSet): SqlIterator = new SqlIterator(conn, stmt, resultSet)
}


