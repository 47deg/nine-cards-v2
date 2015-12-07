package com.fortysevendeg.ninecardslauncher.services.persistence.models

import com.fortysevendeg.ninecardslauncher.commons.contentresolver.IterableCursor
import com.fortysevendeg.ninecardslauncher.repository.model.{App => RepoApp, DockApp => RepoDockApp}
import com.fortysevendeg.ninecardslauncher.services.persistence.conversions.{AppConversions, DockAppConversions}

class IterableApps(cursor: IterableCursor[RepoApp])
  extends IterableCursor[App]
  with AppConversions {

  override def count(): Int = cursor.count()

  override def moveToPosition(pos: Int): App = toApp(cursor.moveToPosition(pos))

  override def close(): Unit = cursor.close()

}

class IterableDockApps(cursor: IterableCursor[RepoDockApp])
  extends IterableCursor[DockApp]
    with DockAppConversions {

  override def count(): Int = cursor.count()

  override def moveToPosition(pos: Int): DockApp = toDockApp(cursor.moveToPosition(pos))

  override def close(): Unit = cursor.close()

}