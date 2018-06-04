package bindong.config

import java.io.File

import cn.epoque.aip.common.log.LogSupport
import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by xxh on 17-12-22.
  */
trait CommonConfig extends LogSupport {
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
