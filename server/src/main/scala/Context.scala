import java.time.LocalDateTime

import akka.actor.{ActorSystem, Props}
import akka.dispatch.ExecutionContexts.global
import akka.util.Timeout
import me.server.domain.appointments.AppointmentsAggregateContext
import me.server.domain_api.appointments_api.{Appointment, CreateAppointment}
import me.server.frontend.http.rest.AppointmentsServiceRoute
import me.server.frontend.{FrontendServer, MainRestService}
import me.server.projections.appointments.AppointmentsProjection
import me.server.projections_api.appointments_api.{AppointmentsProjectionQueryApi, GetAllAppointments}
import me.server.utils.ddd.{AggregateManager, OrganizationId}
import me.server.utils.documentStore.MockDocumentStore

import scala.concurrent.duration._

class Context {

  def init() = {
    implicit val system = ActorSystem("System")
    implicit val timeout = Timeout(25 seconds)

    implicit val ec = global

    //stores
    val appointmentsDocumentStore = new MockDocumentStore[Appointment]

    //projections
    val appointmentsProjection = system.actorOf(Props(new AppointmentsProjection("AppointmentsProjection","AppointmentManager",appointmentsDocumentStore)))
    val appointmentsProjectionQueryApi = new AppointmentsProjectionQueryApi(appointmentsProjection)

    //context
    val appointmentsContextActor = new AppointmentsAggregateContext()
    val appointmentsCommandHandler = system.actorOf(Props(new AggregateManager[Appointment]("ReservationManager",appointmentsContextActor, appointmentsDocumentStore)),"AppointmentsManagerActor")

    //appointmentsCommandHandler ! CreateAppointment(OrganizationId(0), "Test", "", LocalDateTime.now(), LocalDateTime.now().plusMinutes(10))

    //services

    val appointmentsServiceRoute = new AppointmentsServiceRoute(appointmentsCommandHandler, appointmentsProjectionQueryApi)
    // roomsServiceRoute = new RoomsServiceRoute(roomsCommandHandler, roomsProjectionQueryApi)
    val mainRestServiceActor = new MainRestService(appointmentsServiceRoute)

    val frontend = new FrontendServer(mainRestServiceActor)(system)


    frontend
  }

  val (frontend:FrontendServer) = init()
}
