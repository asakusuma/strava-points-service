package actors

import akka.actor.Actor
import akka.actor.Props
import akka.event.Logging
import akka.util.Timeout
import controllers.Application._
import play.api.libs.ws.WS
import play.api.Play.current
import akka.pattern.ask
import scala.concurrent.duration._
import scala.concurrent._
import akka.pattern.pipe

/**
 * Created by akusuma on 9/22/14.
 */
class UpdateMasterActor extends Actor {
  import context._
  implicit val timeout = Timeout(5 seconds)
  val log = Logging(context.system, this)
  def receive = {
    case x: Int  => WS.url("https://www.strava.com/api/v3/athletes/" + x + "?access_token=fce42d5b320b62e955a5739d69e343a64499b74b").get().map { response =>
      response.json
    } pipeTo sender()
    case _
      => "Error"
  }
}
