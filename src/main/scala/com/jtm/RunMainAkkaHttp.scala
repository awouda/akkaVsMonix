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

  def result(i:Int) = downloader.response(i).map{ result =>

    val took = (System.currentTimeMillis() - start ) / 1000
    s"Akka http result run # $i : $result took $took"
  }


   def close() =   {
    println("terminating systems (akka)")
   // sttpBackend.close()
    system.terminate()

  }



}
