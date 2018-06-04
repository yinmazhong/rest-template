package bindong.utils.db

import bindong.utils.db.Executor.CommandListExecutor

/**
  * Created by xubanxian on 17-6-20.
  * more comments,less complains.
  */
class SqlList(val sqlStatements: List[String]) extends CommandListExecutor{

}

object SqlList {
  def apply(sqlStatements:List[String]): SqlList = new SqlList(sqlStatements)

  implicit def toSql(statements: List[String]): SqlList = new SqlList(statements)
}
