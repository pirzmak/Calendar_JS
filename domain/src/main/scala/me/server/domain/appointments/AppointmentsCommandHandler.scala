package me.server.domain.appointments

import me.server.domain_api.appointments_api._
import me.server.utils.cqrs.{CommandFailure, CommandResponse, CommandSuccess}

object AppointmentsCommandHandler {
  def handleCreateAppointment(c: CreateAppointment): CommandResponse = {
    if(!c.start.isBefore(c.end))
      CommandFailure("Date error")
    else
    CommandSuccess(AppointmentCreated(c.title, c.description, c.start, c.end))
  }

  def handleAppointmentChange(c: ChangeAppointment): CommandResponse = {
    CommandSuccess(AppointmentChanged(c.title, c.description, c.start, c.end))
  }

  def handleDeleteAppointment(c: DeleteAppointment): CommandResponse = {
    CommandSuccess(AppointmentDeleted())
  }

  def handleActiveAppointment(c: ActiveAppointment): CommandResponse = {
    CommandSuccess(AppointmentActived())
  }
}
