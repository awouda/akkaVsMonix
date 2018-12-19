package com.jtm

import java.nio.ByteBuffer

import com.softwaremill.sttp._
import monix.eval.Task
import monix.reactive.Observable

import scala.concurrent.duration.Duration

class MonixDownloader(implicit sttpBackend: SttpBackend[Task, Observable[ByteBuffer]] ) {

  def response(i:Int): Task[String] = {
    val response = sttp
      .get(uri"http://ftp.nluug.nl/os/Linux/distr/archlinux/community/os/x86_64/argyllcms-2.0.1-1-x86_64.pkg.tar.xz")
      .response(asStream[Observable[ByteBuffer]])
      .readTimeout(Duration.Inf)
      .send()
    response.flatMap { response =>
      response.body match {
        case Right(obs) =>
          handleBuffer(obs).map { total => s"Run # ${i} Total bytes (monix) = ${total}" }.firstL
//          handleBuffer(obs).map { total => s"Total bytes = ${total}" }.firstL.doOnFinish(_ => Task(sttpBackend.close()))
        case Left(err) => println(err)
          throw new Exception(err)
      }
    }

  }

  private def handleBuffer(obs: Observable[ByteBuffer]) = {
    obs.foldLeft(0){(acc, cur) =>
      acc + cur.array().size}
  }.doOnNext(_ => Task(println("Monix download got all bytes ")))


}
