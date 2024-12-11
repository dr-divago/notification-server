package domain

import org.apache.pekko.actor.typed.{ActorRef, ActorSystem}
import org.apache.pekko.http.scaladsl.server.Directives.{entity, path, post}
import org.apache.pekko.http.scaladsl.server.{Directives, Route}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import scala.concurrent.ExecutionContext

object HttpRoutes:
  trait JsonProtocol extends DefaultJsonProtocol:
    given RootJsonFormat[SubscriptionRequest] = jsonFormat2(SubscriptionRequest.apply)
    given RootJsonFormat[SubscriptionResponse] = jsonFormat1(SubscriptionResponse.apply)

  case class SubscriptionRequest(deviceId: String, eventType: String)
  case class SubscriptionResponse(status: String)

  def apply(registry: ActorRef[RegistryActor.Command])(using system: ActorSystem[_]): Route =
    import JsonProtocol.*
    import org.apache.pekko.http.scaladsl.marshallers.sprayjson.SprayJsonSupport.*

    // Convert futures to thread pool
    given ExecutionContext = system.executionContext

    Directives.concat(
      path("subscribe") {
        post {
          entity(Directives.as[SubscriptionRequest]) { request =>
            registry ! RegistryActor.Command.RegisterFrontend(
              request.eventType,
              request.deviceId
            )

            complete(SubscriptionResponse("success"))
          }
        }
      },

      path("unsubscribe") {
        post {
          entity(Directives.as[SubscriptionRequest]) { request =>
            registry ! RegistryActor.Command.UnregisterFrontend(
              request.eventType,
              request.deviceId
            )

            complete(SubscriptionResponse("success"))
          }
        }
      }
    )