import java.time.LocalDateTime

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import akka.dispatch.ExecutionContexts.global
import akka.testkit.{ImplicitSender, TestKit}
import me.server.domain.appointments.AppointmentsAggregateContext
import me.server.domain_api.appointments_api.{Appointment, ChangeAppointment, CreateAppointment, DeleteAppointment}
import me.server.utils.ddd.{AggregateId, AggregateVersion, OrganizationId}
import me.server.utils.cqrs.{CommandResult, StatusResponse}
import me.server.utils.documentStore.MockDocumentStore
import me.server.utils.tests.TestAggregateRepositoryActor

import scala.concurrent.duration._
import org.scalatest._


class AppointmentsSpec() extends TestKit(ActorSystem("AppointmentsSpec")) with ImplicitSender with GivenWhenThen
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  implicit val ec = global

  implicit val timeout = Timeout(5 seconds)

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }


  val appAggContext = new AppointmentsAggregateContext()
  val documentStore = new MockDocumentStore[Appointment]

  When("Actor appointment added msg")
  "An appointment actor" must {

    "get result with 1 aggregate and 1 version" in {
      val commandHandler = system.actorOf(Props(new TestAggregateRepositoryActor[Appointment](AggregateId(-1),OrganizationId(0),List.empty,appAggContext,documentStore)))

      commandHandler ! CreateAppointment(OrganizationId(0), "mail","pas", LocalDateTime.MIN, LocalDateTime.MAX)
      expectMsgPF() {
        case CommandResult(StatusResponse.success, AggregateId(-1), AggregateVersion(1), "") => ()
      }
    }
  }

  When("Actor user updated msg")
  "An User actor" must {

    "get result with 1 aggregate and 2 version" in {
      val commandHandler = system.actorOf(Props(new TestAggregateRepositoryActor[Appointment](AggregateId(-1),OrganizationId(0),List.empty,appAggContext,documentStore)))

      commandHandler ! CreateAppointment(OrganizationId(0), "mail","pas", LocalDateTime.MIN, LocalDateTime.MAX)
      expectMsg(CommandResult(StatusResponse.success, AggregateId(-1), AggregateVersion(1), ""))
      commandHandler ! ChangeAppointment(AggregateId(-1), AggregateVersion(1), OrganizationId(0), "mail","pas", LocalDateTime.MIN, LocalDateTime.MAX)
      expectMsg(CommandResult(StatusResponse.success, AggregateId(-1), AggregateVersion(2), ""))
    }
  }

  "get result with wrong date" in {
    val commandHandler = system.actorOf(Props(new TestAggregateRepositoryActor[Appointment](AggregateId(-1),OrganizationId(0),List.empty,appAggContext,documentStore)))

    commandHandler ! CreateAppointment(OrganizationId(0), "mail","pas", LocalDateTime.MAX, LocalDateTime.MAX)

    expectMsgPF() {
      case CommandResult(StatusResponse.failure, _, _, _) => ()
    }
  }

  "get result with wrong version" in {
    val commandHandler = system.actorOf(Props(new TestAggregateRepositoryActor[Appointment](AggregateId(-1),OrganizationId(0),List.empty,appAggContext,documentStore)))

    commandHandler ! CreateAppointment(OrganizationId(0), "mail","pas", LocalDateTime.MIN, LocalDateTime.MAX)
    expectMsg(CommandResult(StatusResponse.success, AggregateId(-1), AggregateVersion(1), ""))
    commandHandler ! ChangeAppointment(AggregateId(-1), AggregateVersion(0), OrganizationId(0), "mail","pas", LocalDateTime.MIN, LocalDateTime.MAX)

    expectMsgPF() {
      case CommandResult(StatusResponse.failure, _, _, _) => ()
    }
  }

  "get result with delete version" in {
    val commandHandler = system.actorOf(Props(new TestAggregateRepositoryActor[Appointment](AggregateId(-1),OrganizationId(0),List.empty,appAggContext,documentStore)))

    commandHandler ! CreateAppointment(OrganizationId(0), "mail","pas", LocalDateTime.MIN, LocalDateTime.MAX)
    expectMsg(CommandResult(StatusResponse.success, AggregateId(-1), AggregateVersion(1), ""))
    commandHandler ! DeleteAppointment(AggregateId(-1), AggregateVersion(0), OrganizationId(0))

    expectMsgPF() {
      case CommandResult(StatusResponse.failure, _, _, _) => ()
    }
  }
}
