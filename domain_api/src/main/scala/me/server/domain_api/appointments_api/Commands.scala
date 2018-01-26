package me.server.domain_api.appointments_api

import java.time.{LocalDate, LocalDateTime}

import me.server.utils.ddd.{AggregateId, AggregateVersion, OrganizationId}
import me.server.utils.cqrs.{Command, CommandResult, FirstCommand, MyCommand}


case class CreateAppointment(organizationId: OrganizationId,
                             title: String, description: String, start: LocalDateTime, end: LocalDateTime) extends FirstCommand[Appointment, CommandResult]

case class ChangeAppointment(aggregateId:AggregateId, expectedVersion: AggregateVersion, organizationId: OrganizationId,
                             title: String, description: String, start: LocalDateTime, end: LocalDateTime) extends Command[Appointment, CommandResult]

case class DeleteAppointment(aggregateId:AggregateId, expectedVersion: AggregateVersion, organizationId: OrganizationId) extends Command[Appointment, CommandResult]

case class ActiveAppointment(aggregateId:AggregateId, expectedVersion: AggregateVersion, organizationId: OrganizationId) extends Command[Appointment, CommandResult]