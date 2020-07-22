package com.softwaremill.demo.live

import zio._
import zio.clock.Clock
import zio.duration._

object TestPrintln extends App {
  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] = {
    val start = System.currentTimeMillis()
    def log(msg: String): UIO[Unit] = UIO {
      val now = System.currentTimeMillis()
      val second = (now - start) / 1000L
      println(s"after ${second}s: $msg")
    }

    def printSleepPrint(sleep: Duration, name: String): ZIO[Clock, Nothing, String] =
      log(s"START: $name") *> URIO.sleep(sleep) *> log(s"DONE: $name") *> UIO(name)

    def printSleepFail(sleep: Duration, name: String): ZIO[Clock, Throwable, String] =
      log(s"START: $name") *> URIO.sleep(sleep) *> log(s"FAIL: $name") *> IO.fail(new RuntimeException(s"FAIL: $name"))

    ZIO.succeed(ExitCode.success)
  }
}
