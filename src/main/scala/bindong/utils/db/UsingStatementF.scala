package bindong.utils.db

import java.sql.{Connection, Statement}

import bindong.common.log.LogSupportProj

/**
  * Created by xubanxian on 17-6-19.
  * more comments,less complains.
  */
trait UsingStatementF extends LogSupportProj{
    def UsingStatementF[T](f: Statement=> T )(connection:Connection):Option[T] = {
      val statement = connection.createStatement
      try{
        Some(f(statement))
      }catch {
        case e: Throwable => logger.error(e.getMessage,e)
          None
      }finally {
        try{
          connection.close()
        }catch {
          case e: Throwable => logger.error(e.getMessage,e)
        }
      }
    }
}
