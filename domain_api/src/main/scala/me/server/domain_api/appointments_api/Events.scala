package me.server.domain_api.appointments_api

import java.time.LocalDateTime

import me.server.utils.cqrs.Event

sealed trait AppointmentEvent extends Event

case class AppointmentCreated(title: String, description: String, start: LocalDateTime, end: LocalDateTime) extends AppointmentEvent

case class AppointmentChanged(title: String, description: String, start: LocalDateTime, end: LocalDateTime) extends AppointmentEvent

case class AppointmentDeleted() extends AppointmentEvent

case class AppointmentActived() extends AppointmentEvent
