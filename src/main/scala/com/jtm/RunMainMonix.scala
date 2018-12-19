package com.jtm

import java.nio.ByteBuffer

import com.softwaremill.sttp.SttpBackend
import com.softwaremill.sttp.asynchttpclient.monix.AsyncHttpClientMonixBackend
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable

class RunMainMonix  {


  val start = System.currentTimeMillis()
  implicit val sttpBackend: SttpBackend[Task, Observable[ByteBuffer]] = AsyncHttpClientMonixBackend()

  val downloader = new MonixDownloader()

  val result = downloader.response.doOnFinish(_ => Task(sttpBackend.close())).map{  response =>
      val took = (System.currentTimeMillis() - start ) / 1000
      println(s"$response took $took")
      "done"
  }.runToFuture





}
