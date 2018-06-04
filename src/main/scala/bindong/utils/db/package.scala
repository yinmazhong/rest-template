package bindong.utils

import java.sql.ResultSet

/**
  * Created by xubanxian on 17-6-20.
  * more comments,less complains.
  */
package object db {
  object ConnectionFactoryPrefs {
  }

  implicit class ResultSetUtil(rs: ResultSet) {
    private val columnCount = rs.getMetaData.getColumnCount

    def rows: List[List[String]] = {
      var valueList: List[List[String]] = List()

      while (rs.next()) {
        val oneLine = (1 to columnCount).map(rs.getString).toList
        valueList = oneLine :: valueList
      }
      valueList.reverse
    }

    def columns: List[String] = {
      (1 to columnCount).map(rs.getMetaData.getColumnLabel).toList
    }
  }
}
