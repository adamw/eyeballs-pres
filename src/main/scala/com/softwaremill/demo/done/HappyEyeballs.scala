package com.softwaremill.demo.done

import zio._
import zio.clock.Clock
import zio.duration._

object HappyEyeballs {
  def apply[R, T](tasks: List[ZIO[R, Throwable, T]], delay: Duration): ZIO[R with Clock, Throwable, T] = tasks match {
    case Nil         => IO.fail(new IllegalStateException("no tasks"))
    case task :: Nil => task
    case task :: otherTasks =>
      Queue.bounded[Unit](1).flatMap { taskFailed =>
        val taskWithSignalOnFailed = task.onError(_ => taskFailed.offer(()))
        val sleepOrFailed = ZIO.sleep(delay).race(taskFailed.take)

        taskWithSignalOnFailed.race(sleepOrFailed *> apply(otherTasks, delay))
      }
  }
}