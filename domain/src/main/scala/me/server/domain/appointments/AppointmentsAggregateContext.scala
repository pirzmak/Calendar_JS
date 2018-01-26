package me.server.domain.appointments

import me.server.domain_api.appointments_api._
import me.server.utils.cqrs._
import me.server.utils.ddd.AggregateContext

class AppointmentsAggregateContext() extends AggregateContext[Appointment] {

  def receiveCommand(command: MyCommand, room: Appointment): CommandResponse = command match {
    case c: CreateAppointment => AppointmentsCommandHandler.handleCreateAppointment(c)
    case c: ChangeAppointment => AppointmentsCommandHandler.handleAppointmentChange(c)
    case c: DeleteAppointment => AppointmentsCommandHandler.handleDeleteAppointment(c)
    case c: ActiveAppointment => AppointmentsCommandHandler.handleActiveAppointment(c)

    case _ => throw CommandException.unknownCommand
  }
  def receiveEvents(event: Event, app: Appointment): Appointment = event match {
    case e: AppointmentCreated => AppointmentsEventHandler.handleAppointmentCreated(e)
    case e: AppointmentChanged => AppointmentsEventHandler.handleAppointmentChanged(e, app)
    case e: AppointmentDeleted => AppointmentsEventHandler.handleAppointmentDeleted(e, app)
    case e: AppointmentActived => AppointmentsEventHandler.handleAppointmentActived(e, app)

    case _ => throw CommandException.unknownEvent
  }

  def initialAggregate(): Appointment = Appointment.empty

}
