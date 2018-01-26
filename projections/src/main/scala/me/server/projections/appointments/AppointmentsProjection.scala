package me.server.projections.appointments

import akka.actor.ActorSystem
import me.server.domain_api.appointments_api.Appointment
import me.server.projections_api.appointments_api.GetAllAppointments
import me.server.utils.cqrs.ProjectionActor
import me.server.utils.ddd.{Aggregate, OrganizationId}
import me.server.utils.documentStore.DocumentStore

import scala.concurrent.ExecutionContext

class AppointmentsProjection(projectionId: String, aggregateId: String, documentStore: DocumentStore[Appointment])
                           (implicit system: ActorSystem, implicit val ec: ExecutionContext) extends ProjectionActor {

  def persistenceId = projectionId

  override val receiveCommand: Receive = {
    case m: GetAllAppointments => sender() ! getAllReservations(m.organizationId)
    case _ => ()
  }

  def getAllReservations(organizationId: OrganizationId): List[Aggregate[Appointment]] = {
    documentStore.getAll(organizationId).filter(!_.aggregate.deleted).toList
  }
}
