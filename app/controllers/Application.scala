package controllers

import play.api._
import play.api.libs.json.Json
import play.api.libs.json.JsObject
import play.api.mvc._
import play.api.libs.ws.WS
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.Play.current
import akka.actor.Actor
import akka.actor.Props
import play.api.libs.concurrent.Akka
import actors.UpdateMasterActor
import akka.pattern.ask
import akka.actor._
import play.mvc.BodyParser.Json
import scala.concurrent.Future
import akka.util.Timeout
import scala.concurrent._
import scala.concurrent.duration._
import play.libs.Akka._

object Application extends Controller {

  def index = Action {
    Ok(
      play.api.libs.json.Json.toJson(Map("status" -> "OK", "message" -> ("Hello, world!")))
    )
  }

  def hello(name: String) = Action {
    Ok(
      play.api.libs.json.Json.toJson(Map("status" -> "OK", "message" -> ("Hello, " + name + "!")))
    )
  }

  def pull = Action.async {
    WS.url("https://www.strava.com/api/v3/athletes/2827392?access_token=fce42d5b320b62e955a5739d69e343a64499b74b").get().map { response =>
      Ok("Last name: " + (response.json \ "lastname").as[String])
    }
  }

  def update = Action.async {
    implicit val timeout = Timeout(5 seconds)
    val actor = Akka.system.actorOf(Props[UpdateMasterActor])
    val future = ask(actor, 2827392).mapTo[JsObject];
    future.map( json =>
      Ok(json.toString())
    )
  }

  def welcome = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}