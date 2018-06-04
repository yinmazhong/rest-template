package bindong.rest

import akka.actor.Actor
import bindong.common.log.LogSupportProj
import bindong.service.{AuthorityConfig, MeasurementItemsService}
import org.json4s.Formats
import spray.http._
import spray.routing.{ExceptionHandler, HttpService}

/**
  * Created by xubanxian on 17-6-14.
  * more comments,less complains.
  */
class MeasurementItemsServiceActor extends Actor with MeasurementItemsServiceRoute {
  override def receive = runRoute(route)

  override implicit def actorRefFactory = context
}

trait MeasurementItemsServiceRoute extends HttpService with LogSupportProj {
  implicit def json4sFormats: Formats = org.json4s.DefaultFormats

  implicit def MeasurementItemsServiceExceptionHandler = ExceptionHandler {
    case ex: Exception =>
      requestUri {
        uri =>
          val msg = ex.getMessage
          complete(msg)
      }
  }

  val route = {
    path("getMeasurementItemsById") {
      detach() {
        get {
          parameter('token, 'id) {
            (token: String, id: String) =>
              complete("get measurement items where id =  " + id)
          }
        }
      }
    } ~
      path("uploadMeasurementItems") {
        post {
          detach() {
            entity(as[FormData]) {
              measurementItems => {
                logger.debug(s"[uploadMeasurementItems] post formdata $measurementItems")
                val measurementItemsService = new MeasurementItemsService()
                val token = AuthorityConfig.authorityToken
                val (sign, data) = (measurementItems.fields(0)._2, measurementItems.fields(1)._2)
                logger.debug(s"[md5]  ${measurementItemsService.getMessageMd5(token + data)}")
                  if (measurementItemsService.getMessageMd5(token + data) == sign) {
                    measurementItemsService.save2Redis(measurementItems) match {
                      case Some(value) if value == "success" => complete(StatusCodes.OK,s"""{"status":1200,"msg":"upload measurementItems success"}""")
                      case  _ => complete(StatusCodes.ExpectationFailed,s"""{"status":1500,"msg":"upload measurementItems failure",retry please}""")
                    }
                  } else {
                    complete(StatusCodes.Forbidden,s"""{"status":1401,"msg":"sign mismatch"}""")
                  }
              }
            }
          }
        }
      }
  }
}
