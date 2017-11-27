package bindong

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future
import scala.io.StdIn
/**
  * Created by xxh on 17-11-22.
  */
object WebServer2 {

  // domain model
  final case class Item(name: String,id:Long)
  final case class Order(items:List[Item])

  //formats for unmarshalling an marshalling
  implicit val itemFormat = jsonFormat2(Item)
  implicit val orderFormat = jsonFormat1(Order)

  //(fake)async database query api

  def fetchItem(itemId: Long):Future[Option[Item]] = ???
  def saveOrder(order: Order):Future[Done] = ???

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("WebServer2-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route =
      get{
        pathPrefix("item"/LongNumber){ id =>
          // there might be no item for a given id
          val maybeItem:Future[Option[Item]] = fetchItem(id)

          onSuccess(maybeItem){
            case Some(item) => complete(item)
            case None => complete(StatusCodes.NotFound)
          }
        }
      } ~
      post{
        path("create-order"){
          entity(as[Order]) {order =>
            val saved:Future[Done] = saveOrder(order)
            onComplete(saved){
              done => complete("order created")
            }
          }
        }
      }
    val bindingFuture = Http().bindAndHandle(route,"localhost",8899)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap (_.unbind()) // trigger unbinding from the port
    .onComplete(_ ⇒ system.terminate()) // and shutdown when done [ （_.unbind（））//触发从端口解除绑定]
  }

}
