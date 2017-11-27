package bindong

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

/**
  * Created by xxh on 17-11-22.
  */
object WebServer3 {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("WebServer3-system")
    implicit val materializer = ActorMaterializer()
  }
}
