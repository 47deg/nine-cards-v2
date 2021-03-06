/*
 * Copyright 2017 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cards.nine.services.persistence.impl

import cards.nine.commons.NineCardExtensions._
import cards.nine.commons.services.TaskService._
import cards.nine.models.types.{FetchAppOrder, OrderByCategory, OrderByInstallDate, OrderByName}
import cards.nine.models.{Application, ApplicationData, IterableAppCursor, IterableApplicationData}
import cards.nine.repository.model.{App => RepositoryApp}
import cards.nine.repository.provider.{AppEntity, NineCardsSqlHelper}
import cards.nine.services.persistence._
import cards.nine.services.persistence.conversions.Conversions

trait AppPersistenceServicesImpl extends PersistenceServices {

  self: Conversions with PersistenceDependencies with ImplicitsPersistenceServiceExceptions =>

  def fetchApps(orderBy: FetchAppOrder, ascending: Boolean = true) = {
    val orderByString = toOrderBy(orderBy, ascending)

    val appSeq = for {
      apps <- appRepository.fetchApps(orderByString)
    } yield apps map toApp

    appSeq.resolve[PersistenceServiceException]
  }

  def findAppByPackage(packageName: String) =
    (for {
      app <- appRepository.fetchAppByPackage(packageName)
    } yield app map toApp).resolve[PersistenceServiceException]

  def fetchAppByPackages(packageNames: Seq[String]) =
    (for {
      app <- appRepository.fetchAppByPackages(packageNames)
    } yield app map toApp).resolve[PersistenceServiceException]

  def addApp(app: ApplicationData) =
    (for {
      app <- appRepository.addApp(toRepositoryAppData(app))
    } yield toApp(app)).resolve[PersistenceServiceException]

  def addApps(app: Seq[ApplicationData]) =
    (for {
      _ <- appRepository.addApps(app map toRepositoryAppData)
    } yield ()).resolve[PersistenceServiceException]

  def deleteAllApps() =
    (for {
      deleted <- appRepository.deleteApps()
    } yield deleted).resolve[PersistenceServiceException]

  def deleteAppsByIds(ids: Seq[Int]) =
    (for {
      deleted <- appRepository.deleteApps(s"${NineCardsSqlHelper.id} IN (${ids.mkString(",")})")
    } yield deleted).resolve[PersistenceServiceException]

  def deleteAppByPackage(packageName: String) =
    (for {
      deleted <- appRepository.deleteAppByPackage(packageName)
    } yield deleted).resolve[PersistenceServiceException]

  def updateApp(app: Application) =
    (for {
      updated <- appRepository.updateApp(toRepositoryApp(app))
    } yield updated).resolve[PersistenceServiceException]

  def fetchIterableApps(orderBy: FetchAppOrder, ascending: Boolean = true) = {
    val orderByString = toOrderBy(orderBy, ascending)

    val appSeq: TaskService[IterableApplicationData] = for {
      iter <- appRepository.fetchIterableApps(orderBy = orderByString)
    } yield new IterableAppCursor[RepositoryApp](iter, toApp)

    appSeq.resolve[PersistenceServiceException]
  }

  def fetchIterableAppsByKeyword(
      keyword: String,
      orderBy: FetchAppOrder,
      ascending: Boolean = true) = {
    val orderByString = toOrderBy(orderBy, ascending)

    val appSeq: TaskService[IterableApplicationData] = for {
      iter <- appRepository.fetchIterableApps(
        where = toStringWhere,
        whereParams = Seq(s"%$keyword%"),
        orderBy = orderByString)
    } yield new IterableAppCursor[RepositoryApp](iter, toApp)

    appSeq.resolve[PersistenceServiceException]
  }

  def fetchAppsByCategory(category: String, orderBy: FetchAppOrder, ascending: Boolean = true) = {
    val orderByString = toOrderBy(orderBy, ascending)

    val appSeq = for {
      apps <- appRepository.fetchAppsByCategory(category = category, orderBy = orderByString)
    } yield apps map toApp

    appSeq.resolve[PersistenceServiceException]
  }

  def fetchIterableAppsByCategory(
      category: String,
      orderBy: FetchAppOrder,
      ascending: Boolean = true) = {
    val orderByString = toOrderBy(orderBy, ascending)

    val appSeq: TaskService[IterableApplicationData] = for {
      iter <- appRepository
        .fetchIterableAppsByCategory(category = category, orderBy = orderByString)
    } yield new IterableAppCursor[RepositoryApp](iter, toApp)

    appSeq.resolve[PersistenceServiceException]
  }

  def fetchAlphabeticalAppsCounter =
    (for {
      counters <- appRepository.fetchAlphabeticalAppsCounter
    } yield toDataCounterSeq(counters)).resolve[PersistenceServiceException]

  def fetchCategorizedAppsCounter =
    (for {
      counters <- appRepository.fetchCategorizedAppsCounter
    } yield toDataCounterSeq(counters)).resolve[PersistenceServiceException]

  def fetchInstallationDateAppsCounter =
    (for {
      counters <- appRepository.fetchInstallationDateAppsCounter
    } yield toDataCounterSeq(counters)).resolve[PersistenceServiceException]

  private[this] def toOrderBy(orderBy: FetchAppOrder, ascending: Boolean): String = {
    val sort = if (ascending) "ASC" else "DESC"
    orderBy match {
      case OrderByName        => s"${AppEntity.name} COLLATE NOCASE $sort"
      case OrderByInstallDate => s"${AppEntity.dateInstalled} $sort"
      case OrderByCategory    => s"${AppEntity.category} $sort, ${AppEntity.name} $sort"
    }
  }

  private[this] def toStringWhere: String = s"${AppEntity.name} LIKE ? "

}
