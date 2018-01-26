package me.server.projections_api.appointments_api

import me.server.utils.ddd.{AggregateId, OrganizationId}

case class GetAllAppointments(organizationId: OrganizationId)

