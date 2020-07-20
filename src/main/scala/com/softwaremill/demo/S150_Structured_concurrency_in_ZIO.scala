package com.softwaremill.demo

import zio.clock.Clock
import zio.{Exit, ExitCode, Fiber, URIO, ZIO, ZScope}
import zio.duration._
import zio.console._

object S150_Structured_concurrency_in_ZIO extends zio.App {
  // use race, zipPar, collectAllPar, ...
  //      ^-- avoid using fibers directly at all

  // or if using fibers: all running fibers will be interrupted when the parent fiber exits
  val forked: URIO[Console with Clock, Fiber.Runtime[Nothing, Unit]] =
    (ZIO.sleep(3.seconds) *> putStrLn("Child done")).fork

  // or, manual scopes:

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    ZScope.make[Exit[Nothing, Unit]].flatMap { openScope =>
      {
        for {
          _ <- putStrLn("Start")
          _ <- (ZIO.sleep(3.seconds) *> putStrLn("Child done"))
            .onExit {
              case Exit.Success(value) => putStrLn("success " + value)
              case Exit.Failure(cause) => putStrLn("failure " + cause)
            }
            .forkIn(openScope.scope)
          _ <- putStrLn("Done")
        } yield ()
      }.ensuring(openScope.close(Exit.unit))
    } *> putStrLn("Outside scope")
  }.map(_ => ExitCode.success)

  // or, daemon fibers, but they are an ugly side-effect
}
