package me.server.projections_api.appointments_api

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import me.server.domain_api.appointments_api.Appointment
import me.server.utils.ddd.Aggregate

import scala.concurrent.Future

class AppointmentsProjectionQueryApi(projection: ActorRef)(implicit akkaTimeout: Timeout) {

  def getAllAppointments(query: GetAllAppointments): Future[List[Aggregate[Appointment]]] = {
    (projection ? query).mapTo[List[Aggregate[Appointment]]]
  }

}
