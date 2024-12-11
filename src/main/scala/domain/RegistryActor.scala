package domain

import cats.effect.IO
import dev.profunktor.redis4cats.RedisCommands
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.actor.typed.{ActorRef, Behavior}

object RegistryActor:
  import cats.effect.unsafe.implicits.global
  enum Command:
    case RegisterFrontend(eventType: String, frontendId: String)
    case UnregisterFrontend(eventType: String, frontendId: String)
    case GetFrontends(eventType: String, replyTo: ActorRef[Response])

  enum Response:
    case Frontends(eventType: String, frontendIds: Set[String])
  def apply(redis: RedisCommands[IO, String, String]): Behavior[Command] =
    Behaviors.setup { context =>
      Behaviors.receiveMessage {
        case Command.RegisterFrontend(eventType, frontendId) =>
          redis.sAdd(s"event:$eventType:frontends", frontendId).unsafeRunSync()
          Behaviors.same
        case Command.UnregisterFrontend(eventType, frontendId) =>
          Behaviors.same
        case Command.GetFrontends(eventType, replyTo) =>
          Behaviors.same
      }
    }

