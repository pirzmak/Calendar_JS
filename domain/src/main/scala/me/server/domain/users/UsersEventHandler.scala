package me.server.domain.users

import me.server.domain.users_api.{PersonInfo, User, UserCreated, UserUpdated}

object UsersEventHandler {
  def handleUserCreated(e: UserCreated): User = {
    User(e.email,e.password,PersonInfo.empty,true)
  }
  def handleUserUpdated(user: User, e: UserUpdated): User = {
    user.copy(loginEmail = e.email.getOrElse(user.loginEmail), password = e.password.getOrElse(user.password))
  }
  def handleUserDeleted(user: User): User = {
    user.copy(active = false)
  }
}
