package com.jtm

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.softwaremill.sttp._

import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}

class AkkaHttpDownloader(implicit val sttpBackend: SttpBackend[Future, Source[ByteString, Any]], implicit val system: ActorSystem) {

  implicit val ec: ExecutionContext = system.dispatcher

  def response = {
    val response = sttp
      .post(uri"http://ftp.gnu.org/gnu/a2ps/a2ps-4.12.tar.gz")
      .response(asStream[Source[ByteString, Any]])
      .readTimeout(Duration.Inf)
      .send()

    response.flatMap { response =>
      response.body match {
        case Right(src) => handleBuffer(src)
        case Left(err) => throw new Exception(err)
      }

    }
  }

  private def handleBuffer(source: Source[ByteString, Any]): Future[Int] = {
    implicit val materializer = ActorMaterializer()
    source.runFold(0) { (acc, curr) =>
      println("Akka got some bytes: "+ curr.size)
      acc + curr.size
    }
  }

}
