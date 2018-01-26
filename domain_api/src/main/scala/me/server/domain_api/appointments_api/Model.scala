package me.server.domain_api.appointments_api

import java.time.{LocalDate, LocalDateTime}

import akka.http.scaladsl.model.DateTime

case class Appointment(title: String, description: String, start: LocalDateTime, end: LocalDateTime, deleted: Boolean)

object Appointment {
  val empty = Appointment("","",LocalDateTime.MIN,LocalDateTime.MAX,false)
}
