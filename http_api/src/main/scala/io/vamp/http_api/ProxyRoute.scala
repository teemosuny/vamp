package io.vamp.http_api

import akka.stream.Materializer
import akka.util.Timeout
import io.vamp.common.akka._
import io.vamp.common.http.HttpApiDirectives
import io.vamp.common.notification.NotificationProvider
import io.vamp.operation.controller.ProxyController

trait ProxyRoute extends ProxyController {
  this: ExecutionContextProvider with ActorSystemProvider with HttpApiDirectives with NotificationProvider ⇒

  implicit def timeout: Timeout

  implicit def materializer: Materializer

  val proxyRoute = pathPrefix("proxy") {
    path(Remaining) { path ⇒
      extractUpgradeToWebSocket { upgrade ⇒ context ⇒ websocket(context, upgrade, path)
      } ~ {
        context ⇒ http(context, path)
      }
    }
  }
}
