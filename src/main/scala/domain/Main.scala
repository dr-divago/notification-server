package domain

import cats.effect.IO
import dev.profunktor.redis4cats.Redis
import dev.profunktor.redis4cats.effect.Log.Stdout.*
import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.actor.typed.scaladsl.Behaviors

import scala.concurrent.ExecutionContext

@main def startApplication(): Unit =
  import cats.effect.unsafe.implicits.global
  given system : ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "event-system")
  given ExecutionContext = system.executionContext

  val config = system.settings.config.getConfig("redis")

  val host = config.getString("host")
  val port = config.getInt("port")
  val password = config.getString("password")

  val redisUrl = if (password.nonEmpty) {
    s"redis://:$password@$host:$port"
  } else {
    s"redis://$host:$port"
  }

  val redis = Redis[IO].utf8(redisUrl).allocated.unsafeRunSync()._1
  
  val registry = system.systemActorOf(
    RegistryActor(redis),
    "registry"
  )


