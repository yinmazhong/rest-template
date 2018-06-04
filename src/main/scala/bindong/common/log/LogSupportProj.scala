package bindong.common.log

import org.apache.logging.log4j.{LogManager, Logger}


/**
  * Created by xubanxian on 17-6-15.
  * more comments,less complains.
  */
trait  LogSupportProj{
  val logger = LogManager.getLogger("DatawareHouseLogger")
  logger.info("gsfg")
  //val logger: Logger = Logger.apply("DatawareHouseLogger")

}
