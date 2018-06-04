package bindong.config

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by xxh on 17-12-22.
  */

object BayConfig extends CommonConfig {
  //new Thread(new Upload2RedShiftNew).run()
  setConfigFiles("serviceaddress.conf")
  val ips = config.getList("cn.epoque.appSide.remoteAccessIP")
  val res = ips.unwrapped().contains("127.0.0.1")
  logger.debug(s"ips:$ips")
  logger.debug(s"res:$res")
  //读取数据库配置信息
  /*DatabaseConfig.redShift = RedShiftDatabaseInfo(
    host = config.getString("redshift.host"),
    port = config.getInt("redshift.port"),
    dbName = config.getString("redshift.dbName"),
    user = config.getString("redshift.user"),
    password = config.getString("redshift.password"),
    driver = config.getString("redshift.driver")
  )
  logger.debug(s"[config] redShift Connection Url:${DatabaseConfig.redShift.url}")
  // 从配置文件中读取token值
  DatabaseConfig.mysql = MysqlDatabaseInfo()
  logger.info(s"[config] mysql Connection Url:${DatabaseConfig.mysql.url}")
  AuthorityConfig.authorityToken = config.getString("token")*/
}

