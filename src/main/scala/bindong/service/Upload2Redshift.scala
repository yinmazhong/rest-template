package bindong.service

import akka.actor.{Actor, Props}
import bindong.ActorSystemObj
import bindong.common.log.LogSupportProj
import bindong.utils.db.{RedisHelper, RedshiftSqlHelper}
import redis.RedisClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
  * Created by xubanxian on 17-7-10.
  * more comments,less complains.
  * talk is cheap,show me the code
  */


//
object Upload2Redshift extends LogSupportProj {
  implicit val akkaSystem = ActorSystemObj.actorSystem
  //val upload2RedshiftSericeActor = akkaSystem.actorOf(Props(new Upload2RedshiftSericeActor))
  val upload2RedshiftActor = akkaSystem.actorOf(Props(new Upload2RedshiftActor))
  /*akkaSystem.scheduler.schedule(0 milliseconds,
    50 milliseconds, upload2RedshiftSericeActor, "createNewUpload2RedshiftActor")*/
   akkaSystem.scheduler.schedule(0 milliseconds,
    50 milliseconds, upload2RedshiftActor, "upload2RedshiftActor")
  RedisHelper.UsingRedis[Long]((redis: RedisClient) => {
    logger.info(s"[Upload2Redshift] count key: measurementItemsLists ")
    val redisListLen = Await.result(redis.llen("measurementItemsLists"), 5 seconds)
    //todo: 如果list 长度发育门阀值，报错，告警
    redisListLen match {
      case i if (i == 0) => logger.info(s"[Upload2Redshift] list is Nil" )
      case i if (i > 0) => logger.info(s"[Upload2Redshift] list length: $i")
        Await.result(redis.rpop("measurementItemsLists"), 5 seconds) match {
          case Some(value) => val setName = value.utf8String
            upload2RedshiftActor ! setName
          case None =>
        }
      case i => logger.error(s"[Upload2Redshift] list length: $i and something error")
    }

    //todo: Timer 定时把前一分钟的 数据 从list 中取出到新的临时的 以时间戳为keys的list中，并将这个list中的数据插入到redshift ,完成后删除临时list
    //todo:一分钟生成一个key,将所有的key 再入队列，定时去取
    //用Timer的话需要建立一个TimerTask类,akka actor有自己的调度器

    //new Timer("Upload2RedshiftTimer",true).schedule()

    redis.lpush("measurementItemsList", "")
    // context.actorOf(Props(new Upload2RedshiftActor), "001")
    redis.lpush("measurementItemsList", "")
  })
  //ActorSystemObj.actorSystem.actorOf(Props(new Upload2RedshiftActor()), "new Upload2RedshiftActor")
}

class Upload2RedshiftServiceActor extends Actor with LogSupportProj {
  override def receive: Receive = {
    case "createNewUpload2RedshiftActor" => {
      RedisHelper.UsingRedis[Long]((redis: RedisClient) => {
        logger.info(s"[Upload2Redshift] count key: measurementItemsLists ")
        val redisListLen = Await.result(redis.llen("measurementItemsLists"), 5 seconds)
        //todo: 如果list 长度发育门阀值，报错，告警
        redisListLen match {
          case i if (i == 0) => context.stop(self)
          case i if (i > 0) => logger.info(s"[Upload2Redshift] list length: $i")
            Await.result(redis.rpop("measurementItemsLists"), 5 seconds) match {
              case Some(value) => val setName = value.utf8String
                context.actorOf(Props(new Upload2RedshiftActor), setName) ! setName
              case None =>
            }
          case i => logger.error(s"[Upload2Redshift] list length: $i and something error")
        }

        //todo: Timer 定时把前一分钟的 数据 从list 中取出到新的临时的 以时间戳为keys的list中，并将这个list中的数据插入到redshift ,完成后删除临时list
        //todo:一分钟生成一个key,将所有的key 再入队列，定时去取
        //用Timer的话需要建立一个TimerTask类,akka actor有自己的调度器

        //new Timer("Upload2RedshiftTimer",true).schedule()

        redis.lpush("measurementItemsList", "")
        // context.actorOf(Props(new Upload2RedshiftActor), "001")
        redis.lpush("measurementItemsList", "")
      })
    }
    case (value:String,"success") => println("insert into redshift ok")
    case (value:String,"failure") => RedisHelper.UsingRedis[Long]((redis: RedisClient) => {redis.lpush("measurementItemsLists",value)})
  }

  /*def uploadSetValue2Redshift: Unit ={
    context.actorOf(Props(new Upload2RedshiftActor), setName) ! setName
  }*/

  /*def batchInsert2Redshift(): Unit ={
    RedisHelper.UsingRedis[Long]((redis: RedisClient) => {
      redis.
    }
  }*/


}

class Upload2RedshiftActor extends Actor with LogSupportProj {
  override def receive: Receive = {
    case value: String => RedisHelper.UsingRedis[Unit]((redis: RedisClient) => {
      Future {
        upload2RedshiftProcess(Await.result(redis.smembers(value), 5 second).map(_.utf8String).toList)
      }/*.onComplete{
        case res: Try[Boolean] => sender() ! (value,res.getOrElse("false"))
        case _ => sender() ! (value,"failure")
      }*/
    })
    case _ => logger.error(s"[Upload2RedshiftActor] message not valid")
  }

  def upload2RedshiftProcess(values: List[String]): Boolean = {
    val measurementItemsList = values.map(formData2MeasurementItems(_))
    val InsertSQLValues = measurementItemsList.map(measurementItems => {
      //val createSql = s"create table if not exists measurementiteminfos(UUID varchar(200),scanDate varchar(200),scanId varchar(200),customerId varchar(200),customerInfo varchar(10240),measurementItemsNum Int,measurementItem varchar(10240),analysisReport varchar(10240))"
      s"('${measurementItems.UUID}','${measurementItems.scanDate}','${measurementItems.scanId}','${measurementItems.customerId}','${measurementItems.customerInfo}','${measurementItems.measurementItemsNum}','${measurementItems.mesurementItemInfos}','${measurementItems.analysisReport}')"
    }.mkString(",")
    )

    logger.debug(s"[upload2RedshiftProcess] insert values: $InsertSQLValues")

    //redshift 不支持自增字段，也不支持主键
    val insertSql = s"insert into measurementiteminfos values $InsertSQLValues"
    logger.debug(s"[upload2RedshiftProcess] insert Sql String: $InsertSQLValues")
    new RedshiftSqlHelper(insertSql).execute()
  }

  override def unhandled(msg:Any): Unit = {
      msg match {
        case _ =>
      }
  }

  protected def formData2MeasurementItems(formDataFieldData: String): MeasurementItems = {
    val formDataFieldDataArray = formDataFieldData.split("\\|\\|")
    val (scanDate, scanId, customerId, customerInfoStr, measurementItemsNum, measurementItemInfoStr, uuIdStr, analysisReportStr) =
      (formDataFieldDataArray(0), formDataFieldDataArray(1), formDataFieldDataArray(2), formDataFieldDataArray(3), formDataFieldDataArray(4), formDataFieldDataArray(5), formDataFieldDataArray(6), formDataFieldDataArray(7))
    MeasurementItems(uuIdStr, scanDate, scanId, customerId, customerInfoStr, measurementItemsNum, measurementItemInfoStr, analysisReportStr)
  }
}
