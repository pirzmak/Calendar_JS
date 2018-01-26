package me.server

import java.time.LocalDateTime

import akka.actor.{ActorSystem, Props}
import akka.dispatch.ExecutionContexts.global
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.Timeout
import me.server.domain.appointments.AppointmentsAggregateContext
import me.server.domain_api.appointments_api.{Appointment, ChangeAppointment, CreateAppointment, DeleteAppointment}
import me.server.projections.appointments.AppointmentsProjection
import me.server.projections_api.appointments_api.GetAllAppointments
import me.server.utils.cqrs.{CommandResult, StatusResponse}
import me.server.utils.ddd.{Aggregate, AggregateId, AggregateVersion, OrganizationId}
import me.server.utils.documentStore.MockDocumentStore
import me.server.utils.tests.TestAggregateRepositoryActor
import org.scalatest._

import scala.concurrent.duration._


class AppointmentsSpec() extends TestKit(ActorSystem("AppointmentsSpec")) with ImplicitSender with GivenWhenThen
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  implicit val ec = global

  implicit val timeout = Timeout(5 seconds)

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }


  val appAggContext = new AppointmentsAggregateContext()
  val documentStore = new MockDocumentStore[Appointment]

  "An Appointment actor" must {

    "get result with 0 aggregate" in {
      val documentStore = new MockDocumentStore[Appointment]
      val commandHandler = system.actorOf(Props(new AppointmentsProjection("Test", "TestP", documentStore)))

      commandHandler ! GetAllAppointments(OrganizationId(0))
      expectMsg(List.empty)
    }

    "get result with 1 aggregate" in {
      val documentStore = new MockDocumentStore[Appointment]
      documentStore.insertDocument(AggregateId(0), AggregateVersion(1), OrganizationId(0), Appointment.empty)
      val commandHandler = system.actorOf(Props(new AppointmentsProjection("Test", "TestP", documentStore)))

      commandHandler ! GetAllAppointments(OrganizationId(0))
      expectMsg(List(Aggregate(AggregateId(0), AggregateVersion(1), OrganizationId(0), Appointment.empty)))
    }

    "get result with 2 aggregate" in {
      val documentStore = new MockDocumentStore[Appointment]
      documentStore.insertDocument(AggregateId(0), AggregateVersion(1), OrganizationId(0), Appointment.empty)
      documentStore.insertDocument(AggregateId(0), AggregateVersion(1), OrganizationId(0), Appointment.empty)
      val commandHandler = system.actorOf(Props(new AppointmentsProjection("Test", "TestP", documentStore)))

      commandHandler ! GetAllAppointments(OrganizationId(0))
      expectMsg(List(Aggregate(AggregateId(0), AggregateVersion(1), OrganizationId(0), Appointment.empty),
        Aggregate(AggregateId(0), AggregateVersion(1), OrganizationId(0), Appointment.empty)))

    }

    "get result with 2 aggregate with different organization" in {
      val documentStore = new MockDocumentStore[Appointment]
      documentStore.insertDocument(AggregateId(0), AggregateVersion(1), OrganizationId(0), Appointment.empty)
      documentStore.insertDocument(AggregateId(0), AggregateVersion(1), OrganizationId(0), Appointment.empty)
      documentStore.insertDocument(AggregateId(0), AggregateVersion(1), OrganizationId(1), Appointment.empty)
      val commandHandler = system.actorOf(Props(new AppointmentsProjection("Test", "TestP", documentStore)))

      commandHandler ! GetAllAppointments(OrganizationId(0))
      expectMsg(List(Aggregate(AggregateId(0), AggregateVersion(1), OrganizationId(0), Appointment.empty),
        Aggregate(AggregateId(0), AggregateVersion(1), OrganizationId(0), Appointment.empty)))
    }

    "get epmty result with different organization" in {
      val documentStore = new MockDocumentStore[Appointment]
      documentStore.insertDocument(AggregateId(0), AggregateVersion(1), OrganizationId(0), Appointment.empty)
      documentStore.insertDocument(AggregateId(0), AggregateVersion(1), OrganizationId(0), Appointment.empty)
      documentStore.insertDocument(AggregateId(0), AggregateVersion(1), OrganizationId(1), Appointment.empty)
      val commandHandler = system.actorOf(Props(new AppointmentsProjection("Test", "TestP", documentStore)))

      commandHandler ! GetAllAppointments(OrganizationId(5))
      expectMsg(List.empty)
    }
  }
}
