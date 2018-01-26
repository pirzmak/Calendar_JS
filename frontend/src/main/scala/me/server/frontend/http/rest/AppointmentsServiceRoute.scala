package me.server.frontend.http.rest

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.SecurityDirectives
import akka.util.Timeout
import akka.pattern.ask
import me.server.domain_api.appointments_api.{ActiveAppointment, ChangeAppointment, CreateAppointment, DeleteAppointment}
import me.server.frontend.json.JsonSupport
import me.server.utils.cqrs.CommandResult
import me.server.projections_api.appointments_api._
import me.server.utils.ddd.{AggregateId, OrganizationId}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class AppointmentsServiceRoute(val appointmentsRepository: ActorRef,
                               val appointmentsProjectionQueryApi: AppointmentsProjectionQueryApi)
                              (implicit executionContext: ExecutionContext) extends SecurityDirectives with JsonSupport {

  implicit val timeout = Timeout(25 seconds)

  val routes = {
    path("get-all"/Segment) { organizationId =>
      get {
        complete(appointmentsProjectionQueryApi.getAllAppointments(GetAllAppointments(OrganizationId(organizationId.toLong))))
      }
    } ~
      path("create") {
        post {
          entity(as[CreateAppointment]) { message =>
            complete((appointmentsRepository ? message).mapTo[CommandResult])
          }
        }
      } ~
      path("change") {
        post {
          entity(as[ChangeAppointment]) { message =>
            complete((appointmentsRepository ? message).mapTo[CommandResult])
          }
        }
      } ~
      path("delete") {
        post {
          entity(as[DeleteAppointment]) { message =>
            complete((appointmentsRepository ? message).mapTo[CommandResult])
          }
        }
      } ~
      path("active") {
        post {
          entity(as[ActiveAppointment]) { message =>
            complete((appointmentsRepository ? message).mapTo[CommandResult])
          }
        }
      }
  }
}
