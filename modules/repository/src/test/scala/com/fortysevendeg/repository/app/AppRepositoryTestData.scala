package com.fortysevendeg.repository.app

import com.fortysevendeg.ninecardslauncher.repository.model.{DataCounter, AppData, App}
import com.fortysevendeg.ninecardslauncher.repository.provider.{AppEntityData, AppEntity}

import scala.util.Random

trait AppRepositoryTestData {

  val testAppId = Random.nextInt(10)
  val testNonExistingAppId = 15
  val testName = Random.nextString(5)
  val testPackageName = Random.nextString(5)
  val testNonExistingPackageName = Random.nextString(5)
  val testClassName = Random.nextString(5)
  val testCategory = Random.nextString(5)
  val testNonExistingCategory = Random.nextString(5)
  val testImagePath = Random.nextString(5)
  val testColorPrimary = Random.nextString(5)
  val testDateInstalled = Random.nextLong()
  val testDateUpdate = Random.nextLong()
  val testVersion = Random.nextString(5)
  val testInstalledFromGooglePlay = Random.nextBoolean()

  val appEntitySeq = createAppEntitySeq(5)
  val appEntity = appEntitySeq(0)
  val appSeq = createAppSeq(5)
  val app = appSeq(0)

  def createAppEntitySeq(num: Int) = List.tabulate(num)(
    i => AppEntity(
      id = testAppId + i,
      data = AppEntityData(
        name = testName,
        packageName = testPackageName,
        className = testClassName,
        category = testCategory,
        imagePath = testImagePath,
        colorPrimary = testColorPrimary,
        dateInstalled = testDateInstalled,
        dateUpdate = testDateUpdate,
        version = testVersion,
        installedFromGooglePlay = testInstalledFromGooglePlay)))

  def createAppSeq(num: Int) = List.tabulate(num)(
    i => App(
      id = testAppId + i,
      data = AppData(
        name = testName,
        packageName = testPackageName,
        className = testClassName,
        category = testCategory,
        imagePath = testImagePath,
        colorPrimary = testColorPrimary,
        dateInstalled = testDateInstalled,
        dateUpdate = testDateUpdate,
        version = testVersion,
        installedFromGooglePlay = testInstalledFromGooglePlay)))

  def createAppValues = Map[String, Any](
    AppEntity.name -> testName,
    AppEntity.packageName -> testPackageName,
    AppEntity.className -> testClassName,
    AppEntity.category -> testCategory,
    AppEntity.imagePath -> testImagePath,
    AppEntity.colorPrimary -> testColorPrimary,
    AppEntity.dateInstalled -> testDateInstalled,
    AppEntity.dateUpdate -> testDateUpdate,
    AppEntity.version -> testVersion,
    AppEntity.installedFromGooglePlay -> testInstalledFromGooglePlay)

  def createAppData = AppData(
    name = testName,
    packageName = testPackageName,
    className = testClassName,
    category = testCategory,
    imagePath = testImagePath,
    colorPrimary = testColorPrimary,
    dateInstalled = testDateInstalled,
    dateUpdate = testDateUpdate,
    version = testVersion,
    installedFromGooglePlay = testInstalledFromGooglePlay)

  val appsDataSequence: Seq[String] =
    Seq(
      "!aaa",
      "2bbb",
      "?ccc",
      "1ddd",
      "#eeee",
      "Abc",
      "Acd",
      "Ade",
      "Bcd",
      "Bde",
      "Bef",
      "Cde")

  val appsDataCounters = Seq(
    DataCounter("#", 5),
    DataCounter("A", 3),
    DataCounter("B", 3),
    DataCounter("C", 1)
  )

  val categoryDataSequence: Seq[String] =
    Seq(
      "COMMUNICATION",
      "COMMUNICATION",
      "COMMUNICATION",
      "GAMES",
      "GAMES",
      "GAMES",
      "SOCIAL",
      "SOCIAL",
      "SOCIAL",
      "SOCIAL",
      "TOOLS",
      "TOOLS")

  val categoryDataCounters = Seq(
    DataCounter("COMMUNICATION", 3),
    DataCounter("GAMES", 3),
    DataCounter("SOCIAL", 4),
    DataCounter("TOOLS", 2)
  )

}
