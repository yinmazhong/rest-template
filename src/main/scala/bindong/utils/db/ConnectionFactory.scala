package bindong.utils.db

import java.sql.{Connection, DriverManager, SQLException}

import bindong.common.log.LogSupportProj

/**
  * Created by xubanxian on 17-6-15.
  * more comments,less complains.
  */
trait ConnectionFactory extends LogSupportProj{
    def configInfo: DatabaseInfo

    def createConnection: Connection = {
      try{
        logger.info(s"configInfo.driver: ${configInfo.driver}")
        Class.forName(configInfo.driver)

        DriverManager.setLoginTimeout(10)
        logger.info(s"database LoginTimeout: " + DriverManager.getLoginTimeout)

        DriverManager.getConnection(configInfo.url, configInfo.user, configInfo.password)
      }catch{
        case e: SQLException =>
          logger.error(s"database connection failed! url:${configInfo.url} ${e.getStackTrace}")
          null
      }
    }
}

trait RedShiftConnectionFactory extends ConnectionFactory{
  override def configInfo: DatabaseInfo = DatabaseConfig.redShift
}

/*object ConnectionFactoryPrefs {
  implicit val redshift = new RedShiftConnectionFactory {}

  //implicit val mysql = new MysqlConnectionFactory {}
}*/
