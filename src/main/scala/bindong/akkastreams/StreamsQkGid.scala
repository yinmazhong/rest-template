package bindong.akkastreams

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.Source

import scala.concurrent.Future

/**
  * Created by xxh on 17-11-23.
  */
object StreamsQkGid extends App{

  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()
  val source:Source[Int, NotUsed] = Source(1 to 100)

  source.runForeach(i => println(i))(materializer)
  val done: Future[Done] = source.runForeach(i => println(i))(materializer)

  implicit val ec = system.dispatcher
  done.onComplete(_ => system.terminate())
}
