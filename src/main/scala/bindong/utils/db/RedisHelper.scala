package bindong.utils.db

import bindong.ActorSystemObj
import redis.RedisClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}


/**
  * Created by xubanxian on 17-6-28.
  * more comments,less complains.
  */
object RedisHelper {
  implicit val akkaSystem = ActorSystemObj.actorSystem
  val redis = RedisClient()

  def UsingRedis[T](f: RedisClient => Future[T]): Future[T] ={
    f(redis)
  }
  def main(args: Array[String]): Unit = {
    val redis = RedisClient()
    println("------------------------------measurementItemsList len start----------------------------------------")
    /*redis.llen("mylist").onComplete {
      case Success(res) => println("success: "+res)
      case Failure(res) => println("failure: "+res)
      case _ => println(".............")
    }*/
    //val futurellen = redis.llen("mylist")
    val res = Await.result(redis.llen("mylist"), 5 seconds)
    println("res: " + res)
    println("------------------------------measurementItemsList len end----------------------------------------")
println("++++++++++++++++++++++++++++++++++++++++++++measurementItemsLists rpop start+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")

    println("res: " + Await.result(redis.rpop("measurementItemsLists"),5 seconds).get.utf8String)
    println("++++++++++++++++++++++++++++++++++++++++++++measurementItemsLists rpop end+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
    //      redis.keys("*")
        val futurePong = redis.ping()
    println("Ping sent!")
    futurePong.map(pong => {
      println(s"Redis replied with a $pong")
    })
    Await.result(futurePong, 5 seconds)

    def futureResult = doSomething(redis)

    Await.result(futureResult, 5 seconds)

    akkaSystem.terminate()

    def doSomething(redis: RedisClient): Future[Boolean] = {
      // launch command set and del in parallel
      val s = redis.set("redis", "is awesome")
      val d = redis.del("i")
      for {
        set <- s
        del <- d
        incr <- redis.incr("i")
        iBefore <- redis.get("i")
        incrBy20 <- redis.incrby("i", 20)
        iAfter <- redis.get("i")
      } yield {
        println("SET redis \"is awesome\"")
        println("DEL i")
        println("INCR i")
        println("INCRBY i 20")
        val ibefore = iBefore.map(_.utf8String)
        val iafter = iAfter.map(_.utf8String)
        println(s"i was $ibefore, now is $iafter")
        iafter == "20"
      }
    }
  }
}
