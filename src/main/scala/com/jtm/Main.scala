package com.jtm

object Main  extends App {

  val runMainAkkaHttp = new RunMainAkkaHttp
  val runMainMonix = new RunMainMonix


  implicit val executionContext = runMainAkkaHttp.system.dispatcher

  val f1 = runMainAkkaHttp.result
  val f2 = runMainMonix.result


  for {
    akka <- f1
    monix <- f2
  } yield println("all done")




}
