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

  def response(i:Int) = {
    val response = sttp
      .get(uri"http://ftp.nluug.nl/os/Linux/distr/archlinux/community/os/x86_64/argyllcms-2.0.1-1-x86_64.pkg.tar.xz")
      .response(asStream[Source[ByteString, Any]])
      .readTimeout(Duration.Inf)
      .send()

    response.flatMap { response =>
      response.body match {
        case Right(src) => handleBuffer(src).map{ bytes => s"Run # ${i} total bytes (akka) = ${bytes}"}
        case Left(err) => throw new Exception(err)
      }

    }
  }

  private def handleBuffer(source: Source[ByteString, Any]): Future[Int] = {
    implicit val materializer = ActorMaterializer()
    source.runFold(0) { (acc, curr) =>
      acc + curr.size
    }.map{ x => println("akka downloader got all remote bytes "); x}
  }

}
