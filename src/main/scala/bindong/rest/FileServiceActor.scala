package bindong.rest

import java.io.ByteArrayInputStream

import akka.actor.Actor
import spray.http.{HttpResponse, MultipartFormData}
import spray.routing.HttpService

/**
  * Created by xubanxian on 17-6-14.
  * more comments,less complains.
  */
class FileServiceActor extends Actor with FileServiceHttpRoute{
  override def receive = runRoute(route)

  override def actorRefFactory = context
}

trait FileServiceHttpRoute extends HttpService {
  val route = {
    path("upload"){
      post{
        entity(as[MultipartFormData]){
          formData => {
            formFields('token.as[String],'userId.as[String],'file.as[Array[Byte]]){
              (token,userId,dataBytes)=>{
                detach(){
                  complete{
                    val data = new ByteArrayInputStream(dataBytes)
                    val fileName = "todo"
                    HttpResponse(200,"upload file success!")
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
