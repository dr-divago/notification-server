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

  val redis = Redis[IO].utf8("redis://localhost").allocated.unsafeRunSync()._1
  
  val registry = system.systemActorOf(
    RegistryActor(redis),
    "registry"
  )


