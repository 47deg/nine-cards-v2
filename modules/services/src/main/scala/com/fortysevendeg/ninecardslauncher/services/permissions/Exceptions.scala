package com.fortysevendeg.ninecardslauncher.services.permissions

import com.fortysevendeg.ninecardslauncher.commons.services.TaskService.NineCardException

case class PermissionsServicesException(message: String, cause: Option[Throwable] = None)
  extends RuntimeException(message)
  with NineCardException {
  cause map initCause
}

trait ImplicitsPermissionsServicesExceptions {

  implicit def permissionsServicesExceptionConverter =
    (t: Throwable) => PermissionsServicesException(t.getMessage, Option(t))
}