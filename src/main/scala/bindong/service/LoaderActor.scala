package bindong.service

import akka.actor.Actor
import bindong.common.log.LogSupportProj

/**
  * Created by xubanxian on 17-7-19.
  * more comments,less complains.
  */
class LoaderActor extends Actor with LogSupportProj{
  override def receive: Receive = {
    case "start" =>  logger.info("[LoaderActor ] LoaderActor Starting")
  }

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


}

case class UploadStart()
