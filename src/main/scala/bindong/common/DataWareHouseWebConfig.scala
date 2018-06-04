package bindong.common

import java.io.File

import bindong.common.log.LogSupportProj
import bindong.service.AuthorityConfig
import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by xubanxian on 17-6-15.
  * more comments,less complains.
  */
object DataWareHouseWebConfig extends CommonConfig {
  //new Thread(new Upload2RedShiftNew).run()
  setConfigFiles("serviceaddress.conf")
//读取数据库配置信息
  /*DatabaseConfig.redShift = RedShiftDatabaseInfo(
    host = config.getString("redshift.host"),
    port = config.getInt("redshift.port"),
    dbName = config.getString("redshift.dbName"),
    user = config.getString("redshift.user"),
    password = config.getString("redshift.password"),
    driver = config.getString("redshift.driver")
  )
  logger.debug(s"[config] redShift Connection Url:${DatabaseConfig.redShift.url}")*/
// 从配置文件中读取token值
  AuthorityConfig.authorityToken = config.getString("token")
}

trait CommonConfig extends LogSupportProj {
  var config: Config = null

  protected var configHome = "../config"

  /*
  * 读取所有配置文件
  * 设置配置文件，只写文件名即可
  *
  * 默认是从configHome路径下读取文件，同时如果resources下有同名的文件也会读取
  * 在前面的文件会覆盖后面的文件中相同key的配置值
  * @param fileNames*/

  def setConfigFiles(fileNames: String*): Unit = {
    logger.info(s"[config ] set configHome : $configHome")
    config = fileNames.toList.map(load).reduce((a, b) => a.withFallback(b))
  }

  protected def load(fileName: String): Config = {
    val resourceFileName = fileName
    val configFile = new File(makePath(fileName))
    if (configFile.exists()) {
      logger.info(s"[config ] Loading　file [${configFile.getPath}] and resource[$resourceFileName]")
      ConfigFactory.parseFile(configFile).withFallback(ConfigFactory.load(resourceFileName))
    }
    else {
      logger.info(s"[config ] loading resource[$resourceFileName]")
      ConfigFactory.load(resourceFileName)
    }
  }

  protected def makePath(fileName: String): String = {
    val newDir = configHome.trim.replace("""\\""", "/")
    if (newDir.endsWith("/")) configHome + fileName
    else configHome + "/" + fileName
  }
}