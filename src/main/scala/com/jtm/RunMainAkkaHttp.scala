package com.jtm

import akka.actor.ActorSystem
import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.softwaremill.sttp.SttpBackend
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Future
import scala.util.{Failure, Success}

class RunMainAkkaHttp extends  {

  val start = System.currentTimeMillis()
  implicit val system = ActorSystem("QuickStart")

  implicit val sttpBackend: SttpBackend[Future, Source[ByteString, Any]] = AkkaHttpBackend.usingActorSystem(system)

  val downloader = new AkkaHttpDownloader()

//  val result = downloader.response.onComplete{
//    case Success(response) =>
//      val took = (System.currentTimeMillis() - start ) / 1000
//      println(s"$response took $took")
//      closeStuff()
//    case Failure(err) => println("got error...")
//      err.printStackTrace()
//  }


  val result = downloader.response.flatMap{ result =>

    val took = (System.currentTimeMillis() - start ) / 1000
    println(s"Akka http result: $result took $took")
    closeStuff()
  }


  private def closeStuff() =   {
    println("terminating systems...")
    sttpBackend.close()
    system.terminate()

  }



}
