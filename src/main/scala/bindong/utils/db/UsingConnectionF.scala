package bindong.utils.db

import java.sql.Connection

import bindong.common.log.LogSupportProj

/**
  * Created by xubanxian on 17-6-16.
  * more comments,less complains.
  */
trait UsingConnectionF extends LogSupportProj {
  def UsingConnectionFB(f: Connection => Boolean)(implicit factory: ConnectionFactory): Boolean = UsingConnectionF[Boolean](f)(factory).getOrElse(false)

  def UsingConnectionFLL(f: Connection => List[List[String]])(implicit factory: ConnectionFactory): List[List[String]] = UsingConnectionF[List[List[String]]](f)(factory).getOrElse(Nil)

  def UsingConnectionF[T](f: Connection => T)(implicit factory: ConnectionFactory): Option[T] = {
    val connection = factory.createConnection
    try {
      Some(f(connection))
    } catch {
      case e: Throwable => logger.error(e.getMessage, e)
        None
    } finally {
      try {
        connection.close()
      } catch {
        case e: Throwable => logger.error(e.getMessage, e)
      }
    }
  }
}
