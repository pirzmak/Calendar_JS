package me.server.frontend.json

import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import me.server.domain_api.appointments_api._
import me.server.domain_api.users_api.{CreateUser, PersonInfo, PersonalData, UserId}
import me.server.utils.cqrs.{CommandResult, StatusResponse}
import me.server.utils.ddd.{Aggregate, AggregateId, AggregateVersion, OrganizationId}
import spray.json._


trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol { //TODO add types
  implicit val aggregateIdJsonFormat = jsonFormat1(AggregateId)
  implicit val organizationIdJsonFormat = jsonFormat1(OrganizationId)
  implicit val versionIdJsonFormat = jsonFormat1(AggregateVersion)

  implicit val statusResponseJsonFormat = jsonFormat1(StatusResponse.apply)
  implicit val commandSuccessResultJsonFormat = jsonFormat4(CommandResult)

  implicit val localDateTimeJsonFormat: JsonFormat[LocalDateTime] =
    new JsonFormat[LocalDateTime] {
      private val formatter = DateTimeFormatter.ISO_DATE_TIME

      override def write(x: LocalDateTime): JsValue = JsString(x.format(formatter))

      override def read(value: JsValue): LocalDateTime = value match {
        case JsString(x) => LocalDateTime.parse(x)
        case x => deserializationError("Wrong time format of " + x)
      }
    }

  //Appointment
  implicit val appointment = jsonFormat5(Appointment.apply)
  implicit val aggregateAppointmetnJsonFormat = jsonFormat4(Aggregate.apply[Appointment])

  implicit val createAppointmentJsonFormat = jsonFormat5(CreateAppointment)
  implicit val changeAppointmentJsonFormat = jsonFormat7(ChangeAppointment)
  implicit val deleteAppointmentJsonFormat = jsonFormat3(DeleteAppointment)
  implicit val activeAppointmentJsonFormat = jsonFormat3(ActiveAppointment)

  //User
  implicit val userIdJsonFormat = jsonFormat1(UserId.apply)
  implicit val createUserJsonFormat = jsonFormat5(CreateUser)
  implicit val personalDataJsonFormat = jsonFormat4(PersonalData.apply)
  implicit val personInfoJsonFormat = jsonFormat5(PersonInfo.apply)

}
