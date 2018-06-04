package bindong.utils.db

import bindong.utils.db.Executor.{CommandExecutor, QueryExecutor, QueryIterator}

/**
  * Created by xubanxian on 17-6-19.
  * more comments,less complains.
  */
class Sql(private[this] val sqlStatements: String*) extends QueryExecutor with CommandExecutor with QueryIterator {
  val sqlStatement: String = sqlStatements.mkString(";")
}

object Sql {
  def apply(sqlStatements: String*) = new Sql(sqlStatements: _*)

  implicit def toSql(statement: String):Sql = new Sql(statement)
}
