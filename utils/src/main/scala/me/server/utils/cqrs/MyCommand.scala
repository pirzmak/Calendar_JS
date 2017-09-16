package me.server.utils.cqrs

import akka.actor.ActorRef
import me.server.utils.ddd.{AggregateId, AggregateVersion}

case class StatusResponse(name: String)

object StatusResponse {
  val success = StatusResponse("SUCCESS")
  val failure = StatusResponse("FAILURE")

  def byName(name: String): StatusResponse = name match {
    case "SUCCESS" => success
    case "FAILURE" => failure
  }
}

trait MyCommand

abstract class FirstCommand[AGGREGATE, COMMAND_RESULT] extends MyCommand

case class CommandWithSender(sender: ActorRef, command: MyCommand)

abstract class Command[AGGREGATE, COMMAND_RESULT] extends MyCommand {
  val aggregateId: AggregateId
  val expectedVersion: AggregateVersion
}

case class CommandResult(status: StatusResponse, id: AggregateId, version: AggregateVersion, message: String)

case class CommandException(msg: String) extends Exception(msg)

object CommandException {
  val unknownCommand = CommandException("UnknownCommand command")
}
