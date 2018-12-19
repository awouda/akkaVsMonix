package com.jtm

import java.nio.ByteBuffer

import com.softwaremill.sttp._
import monix.eval.Task
import monix.reactive.Observable

import scala.concurrent.duration.Duration

class MonixDownloader(implicit sttpBackend: SttpBackend[Task, Observable[ByteBuffer]] ) {

  def response: Task[String] = {
    val response = sttp
      .post(uri"http://ftp.gnu.org/gnu/a2ps/a2ps-4.12.tar.gz")
      .response(asStream[Observable[ByteBuffer]])
      .readTimeout(Duration.Inf)
      .send()
    response.flatMap { response =>
      response.body match {
        case Right(obs) =>
          handleBuffer(obs).map { total => s"Total bytes = ${total}" }.firstL.doOnFinish(_ => Task(sttpBackend.close()))
        case Left(err) => println(err)
          throw new Exception(err)
      }
    }

  }

  private def handleBuffer(obs: Observable[ByteBuffer]) = {
    obs.foldLeft(0){(acc, cur) =>
      println("Monix got some bytes: "+cur.array().size)

      acc + cur.array().size}
  }


}
