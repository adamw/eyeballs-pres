package com.softwaremill.demo.live

import java.net.{InetAddress, Socket}

import zio._
import zio.blocking._
import zio.console._
import zio.duration._

object TestSocket extends App {
  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] = {
    ZIO.succeed(ExitCode.success)
  }
}
