package bindong.service

import akka.actor.{Actor, ActorSystem}
import bindong.common.log.LogSupportProj
import bindong.utils.db.RedshiftSqlHelper
import redis.RedisClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
  * Created by xubanxian on 17-7-14.
  * more comments,less complains.
  */
class Upload2RedShiftNew extends LogSupportProj with Actor {
  // 不用akka模型实现一个先
  implicit val akkaSystem = ActorSystem("shoes-epoque-datawarehouse-upload2RedShift")
  val redis = RedisClient()

  // 从key list 中取出 最右边一个key
  def getSetName(): String = {
    logger.info(s"[Upload2Redshift] count key: measurementItemsLists ")
    val redisListLen = Await.result(redis.llen("measurementItemsLists"), 25 seconds)
    //todo: 如果list 长度发育门阀值，报错，告警
    redisListLen match {
      case i if (i == 0) => logger.info(s"[Upload2Redshift] list is Nil")
        ""
      case i if (i > 0) => logger.info(s"[Upload2Redshift] list length: $i")
        Await.result(redis.rpop("measurementItemsLists"), 25 seconds) match {
          case Some(value) => value.utf8String
          case None => ""
        }
      case i => logger.error(s"[Upload2Redshift] list length: $i and something error")
        ""
    }

  }
//todo: 测试，模拟真实环境 测试
  //单线程。如果没处理完的话就阻塞
 /* while (true) {
    println("---------------------------------upload2redshiftnew------------------------------")
    var setName = getSetName()
    val result = Future {
      if (setName != "") {
        upload2RedshiftProcess(Await.result(redis.smembers(setName), 5 second).map(_.utf8String).toList)
      } else {
        Thread.sleep(1000 * 10)
      }
    }
    Await.result(result, 25 seconds) match {
      case true => logger.info(s"[upload2Redshift] set: $setName insert into Redshift ok")
      case _ => redis.lpush("measurementItemsLists", setName)
        logger.info(s"[upload2Redshift] set: $setName insert into Redshift false and reinsert $setName into list measurementItemsLists")
    }
  }*/

  def upload2RedshiftProcess(values: List[String]): Boolean = {
    val measurementItemsList = values.map(formData2MeasurementItems(_))
    val InsertSQLValues = measurementItemsList.map(measurementItems => {
      s"('${measurementItems.UUID}','${measurementItems.scanDate}','${measurementItems.scanId}','${measurementItems.customerId}','${measurementItems.customerInfo}','${measurementItems.measurementItemsNum}','${measurementItems.mesurementItemInfos}','${measurementItems.analysisReport}')"
    }.mkString(",")
    )

    logger.debug(s"[upload2RedshiftProcess] insert values: $InsertSQLValues")

    //redshift 不支持自增字段，也不支持主键
    val insertSql = s"insert into measurementiteminfos values $InsertSQLValues"
    logger.debug(s"[upload2RedshiftProcess] insert Sql String: $InsertSQLValues")
    new RedshiftSqlHelper(insertSql).execute()
  }


  protected def formData2MeasurementItems(formDataFieldData: String): MeasurementItems = {
    val formDataFieldDataArray = formDataFieldData.split("||")
    val (scanDate, scanId, customerId, customerInfoStr, measurementItemsNum, measurementItemInfoStr, uuIdStr, analysisReportStr) =
      (formDataFieldDataArray(0), formDataFieldDataArray(1), formDataFieldDataArray(2), formDataFieldDataArray(3), formDataFieldDataArray(4), formDataFieldDataArray(5), formDataFieldDataArray(6), formDataFieldDataArray(7))
    MeasurementItems(uuIdStr, scanDate, scanId, customerId, customerInfoStr, measurementItemsNum, measurementItemInfoStr, analysisReportStr)
  }

  def happyRun(): Unit = {
    while (true) {
      Thread.sleep(1000 * 50)
      println("---------------------------------upload2redshiftnew------------------------------")
      var setName = getSetName()
      val result = Future {
        if (setName != "") {
          Thread.sleep(1000 * 50)
          logger.info(s"[happyRun ]"+Await.result(redis.smembers(setName), 5 second).map(_.utf8String).toList)
        } else {
          Thread.sleep(1000 * 100)
        }
      }
     /* Await.result(result, 25 seconds) match {
        case true => logger.info(s"[upload2Redshift] set: $setName insert into Redshift ok")
        case _ => redis.lpush("measurementItemsLists", setName)
          logger.info(s"[upload2Redshift] set: $setName insert into Redshift false and reinsert $setName into list measurementItemsLists")
      }*/
    }
  }

 override def receive: Receive = {
    case "start" =>  logger.info("[LoaderActor ] LoaderActor Starting")
     happyRun()
  }

}

