package me.server.domain.appointments

import me.server.domain_api.appointments_api._

object AppointmentsEventHandler {
  def handleAppointmentCreated(e: AppointmentCreated): Appointment = {
    Appointment(e.title,e.description,e.start,e.end,false)
  }

  def handleAppointmentChanged(e: AppointmentChanged, app: Appointment): Appointment = {
    app.copy(e.title,e.description,e.start,e.end,false)
  }

  def handleAppointmentDeleted(e: AppointmentDeleted, app: Appointment): Appointment = {
    app.copy(deleted = true)
  }

  def handleAppointmentActived(e: AppointmentActived, app: Appointment): Appointment = {
    app.copy(deleted = true)
  }
}

