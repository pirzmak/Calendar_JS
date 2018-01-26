package me.server.frontend

import akka.http.scaladsl.server.Directives._
import me.server.frontend.http.rest.AppointmentsServiceRoute
import me.server.utils.CorsSupport

class MainRestService(appointmentsServiceRoute: AppointmentsServiceRoute) extends CorsSupport {

  val routes  =
    pathPrefix("") {
      corsHandler {
        pathPrefix("appointments") {
          appointmentsServiceRoute.routes
        }
      }
//      ~corsHandler {
//        pathPrefix("users") {
//          usersServiceRoute.routes
//        }
//      }
    }
}
