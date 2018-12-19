package com.jtm

import scala.concurrent.Future


object Main extends App {

  val runMainAkkaHttp = new RunMainAkkaHttp
  val runMainMonix = new RunMainMonix

  implicit val executionContext = runMainAkkaHttp.system.dispatcher


  val results1 = for {
    akka <- runMainAkkaHttp.result(1)
    monix <- runMainMonix.result(1)
  } yield List(akka, monix)


  val results2 = for {
    akka <- runMainAkkaHttp.result(2)
    monix <- runMainMonix.result(2)
  } yield List(akka, monix)



   results1.foreach{ res => println("results from run 1:")
    res.foreach(println)
   }


  val allDone = results2.flatMap{ res => println("results from run 2:")
    res.foreach(println)
    runMainAkkaHttp.close().flatMap(_ => Future.successful(runMainMonix.close()))
  }


  allDone.onComplete(_ => println("all done, exiting"))


  //  results.onComplete{ results =>
  //    println("second run:")
  //    results.get.foreach(println) // unsafe!! for now ok.
  //    for {
  //      closeMonix <- Future.successful(runMainMonix.close())
  //      closeakka <- runMainAkkaHttp.close()
  //    } yield println("exiting now")

  //  }


}
