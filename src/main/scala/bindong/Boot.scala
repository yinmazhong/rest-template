package bindong

import javax.servlet.ServletContext

import akka.actor.{ActorRef, ActorSystem, Props}
import bindong.common.DataWareHouseWebConfig
import bindong.common.log.LogSupportProj
import bindong.rest.{FileServiceActor, MeasurementItemsServiceActor}
import spray.routing.HttpServiceActor
import spray.servlet.WebBoot
/**
  * Created by xubanxian on 17-6-8.
  * more comments,less complains.
  */
class Boot(ctx:ServletContext) extends WebBoot with LogSupportProj{
  // 读取配置文件
  DataWareHouseWebConfig
  // 定时将redis中的数据入库redshift

  override def system: ActorSystem = ActorSystemObj.actorSystem
  //脚型3D文件上传服务
  val fileService = system.actorOf(Props[FileServiceActor])
  //脚型维度上传服务
  val measurementItemsService = system.actorOf(Props[MeasurementItemsServiceActor])

  //脚型维度信息从redis中上传到redshift中

  //val upload2RedshiftService = system.actorOf(Props[Upload2RedShiftNew])

  //upload2RedshiftService ! "start"

  class RootServiceActor extends HttpServiceActor{
    override def receive: Receive = runRoute {
      pathPrefix("dlo3dScan"/"file"){
        fileService ! _
      } ~
      pathPrefix("dlo3dScan"/"measurementItems"){
        measurementItemsService ! _
      }
    }
  }

  override def serviceActor: ActorRef = system.actorOf(Props(new RootServiceActor))

  // todo :另外新建一个Actor System
  //Upload2RedShiftNew

}


// Actor System 是一个很重的结构，它会分配一到N个线程，所以对每一个逻辑应用建立一个就好了
// 接口用一个Actor System 用以处理 请求 ，对于定时入库模块另开一个 Actor System
/*每个actor 大约 300 bit = 300/8 byte
* 那么1G内存能够运行1024*1024*1024*8/300 大约 28000k个actor*/
object ActorSystemObj {
  implicit val actorSystem = ActorSystem("bay")
}
