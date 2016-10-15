package cards.nine.process.device.impl

import cards.nine.commons._
import cards.nine.commons.contentresolver.IterableCursor
import cards.nine.commons.test.data.ApplicationTestData
import cards.nine.models
import cards.nine.models.NineCardsIntentImplicits._
import cards.nine.models.types._
import cards.nine.models._
import cards.nine.process.device.models.{LastCallsContact, _}
import cards.nine.repository.model.{App => RepositoryApp}
import cards.nine.services.api.{CategorizedPackage, RequestConfig}
import cards.nine.services.persistence.models.{IterableApps => ServicesIterableApps}
import play.api.libs.json.Json

trait DeviceProcessData
  extends ApplicationTestData 
  with NineCardsIntentConversions {

  val statusCodeOk = 200
  val items = 5

  val size = 4

  val name1 = "Scala Android"
  val packageName1 = "com.fortysevendeg.scala.android"
  val className1 = "ScalaAndroidActivity"
  val path1 = "/example/path1"
  val category1 = Game
  val imagePath1 = "imagePath1"
  val dateInstalled1 = 1L
  val dateUpdate1 = 1L
  val version1 = "22"
  val installedFromGooglePlay1 = true

  val name2 = "Example"
  val packageName2 = "com.fortysevendeg.example"
  val className2 = "ExampleActivity"
  val path2 = "/example/path2"
  val category2 = BooksAndReference
  val imagePath2 = "imagePath1"
  val dateInstalled2 = 1L
  val dateUpdate2 = 1L
  val version2 = "22"
  val installedFromGooglePlay2 = true

  val name3 = "Scala Api"
  val packageName3 = "com.fortysevendeg.scala.api"
  val className3 = "ScalaApiActivity"
  val path3 = "/example/path3"
  val category3 = Business
  val imagePath3 = "imagePath1"
  val dateInstalled3 = 1L
  val dateUpdate3 = 1L
  val version3 = "22"
  val installedFromGooglePlay3 = true

  val name4 = "Last App"
  val packageName4 = "com.fortysevendeg.scala.last"
  val className4 = "LastAppActivity"
  val path4 = "/example/path4"
  val category4 = Comics
  val imagePath4 = "imagePath1"
  val dateInstalled4 = 1L
  val dateUpdate4 = 1L
  val version4 = "22"
  val installedFromGooglePlay4 = true

  val userHashCode1 = 1
  val autoAdvanceViewId1 = 1
  val initialLayout1 = 1
  val minHeight1 = 40
  val minResizeHeight1 = 40
  val minResizeWidth1 = 40
  val minWidth1 = 40
  val resizeMode1 = 1
  val updatePeriodMillis1 = 1
  val label1 = "label1"
  val preview1: Int = 1

  val userHashCodeOption1 = Option(userHashCode1)

  val userHashCode2 = 2
  val autoAdvanceViewId2 = 2
  val initialLayout2 = 2
  val minHeight2 = 110
  val minResizeHeight2 = 110
  val minResizeWidth2 = 110
  val minWidth2 = 110
  val resizeMode2 = 2
  val updatePeriodMillis2 = 2
  val label2 = "label2"
  val preview2: Int = 2

  val userHashCodeOption2 = Option(userHashCode2)

  val userHashCode3 = 3
  val autoAdvanceViewId3 = 3
  val initialLayout3 = 3
  val minHeight3 = 180
  val minResizeHeight3 = 180
  val minResizeWidth3 = 180
  val minWidth3 = 180
  val resizeMode3 = 3
  val updatePeriodMillis3 = 3
  val label3 = "label3"
  val preview3: Int = 3

  val userHashCodeOption3 = Option(userHashCode3)

  val phoneNumber1 = "+00 111 222 333"
  val contactName1 = "Contact 1"
  val numberType1 = PhoneHome
  val date1 = 3L
  val callType1 = IncomingType
  val lookupKey1 = "lookupKey 1"
  val photoUri1 = "photoUri 1"

  val phoneNumber2 = "+00 444 555 666"
  val contactName2 = "Contact 2"
  val numberType2 = PhoneWork
  val date2 = 2L
  val callType2 = OutgoingType
  val lookupKey2 = "lookupKey 2"
  val photoUri2 = "photoUri 2"

  val phoneNumber3 = "+00 777 888 999"
  val contactName3 = "Contact 3"
  val numberType3 = PhoneOther
  val date3 = 1L
  val callType3 = MissedType
  val lookupKey3 = "lookupKey 3"
  val photoUri3 = "photoUri 3"

  val requestConfig = RequestConfig("fake-api-key", "fake-session-token", "fake-android-id", Some("fake-android-token"))

  val packageNameForCreateImage = "com.example"

  val pathForCreateImage = "/example/for/create/image"

  val urlForCreateImage = "http://www.w.com/image.jpg"

  val categorizedPackage = CategorizedPackage(
    packageName = packageNameForCreateImage,
    category = Some("SOCIAL"))

  val intentStr = """{ "className": "classNameValue", "packageName": "packageNameValue", "categories": ["category1"], "action": "actionValue", "extras": { "pairValue": "pairValue", "empty": false, "parcelled": false }, "flags": 1, "type": "typeValue"}"""
  val intent = Json.parse(intentStr).as[NineCardsIntent]

  val shortcuts: Seq[Shortcut] = Seq(
    Shortcut(
      title = "Shortcut 1",
      icon = None,
      intent = intent),
    Shortcut(
      title = "Shortcut 1",
      icon = None,
      intent = intent),
    Shortcut(
      title = "Shortcut 1",
      icon = None,
      intent = intent))

  val contacts: Seq[Contact] = Seq(
   Contact(
      name = contactName1,
      lookupKey = lookupKey1,
      photoUri = photoUri1,
      hasPhone = false,
      favorite = false,
      info = None),
   Contact(
      name = contactName2,
      lookupKey = lookupKey2,
      photoUri = photoUri2,
      hasPhone = false,
      favorite = false,
      info = None),
   Contact(
      name = contactName3,
      lookupKey = lookupKey3,
      photoUri = photoUri3,
      hasPhone = false,
      favorite = false,
      info = None))

  val pathShortcut = "/example/shortcut"

  val nameShortcut = "aeiou-12345"

  val fileNameShortcut = s"$pathShortcut/$nameShortcut"

  val lookupKey = "lookupKey 1"

  val keyword = "keyword"

  val contact = Contact(
    name = "Simple Contact",
    lookupKey = lookupKey,
    photoUri = photoUri1,
    hasPhone = true,
    favorite = false,
    info = Some(
     ContactInfo(
        emails = Seq(
          ContactEmail(
            address = "sample1@47deg.com",
            category = EmailHome
          )
        ),
        phones = Seq(
          ContactPhone(
            number = phoneNumber1,
            category = PhoneHome
          ),
          ContactPhone(
            number = phoneNumber2,
            category = PhoneMobile
          )
        )
      )
    ))

  val widgetsServices: Seq[models.AppWidget] = Seq(
    AppWidget(
      userHashCode = userHashCodeOption1,
      autoAdvanceViewId = autoAdvanceViewId1,
      initialLayout = initialLayout1,
      minHeight = minHeight1,
      minResizeHeight = minResizeHeight1,
      minResizeWidth = minResizeWidth1,
      minWidth = minWidth1,
      className = className1,
      packageName = packageName1,
      resizeMode = WidgetResizeMode(resizeMode1),
      updatePeriodMillis = updatePeriodMillis1,
      label = label1,
      preview = preview1),
    AppWidget(
      userHashCode = userHashCodeOption2,
      autoAdvanceViewId = autoAdvanceViewId2,
      initialLayout = initialLayout2,
      minHeight = minHeight2,
      minResizeHeight = minResizeHeight2,
      minResizeWidth = minResizeWidth2,
      minWidth = minWidth2,
      className = className2,
      packageName = packageName2,
      resizeMode = WidgetResizeMode(resizeMode2),
      updatePeriodMillis = updatePeriodMillis2,
      label = label2,
      preview = preview2),
    AppWidget(
      userHashCode = userHashCodeOption3,
      autoAdvanceViewId = autoAdvanceViewId3,
      initialLayout = initialLayout3,
      minHeight = minHeight3,
      minResizeHeight = minResizeHeight3,
      minResizeWidth = minResizeWidth3,
      minWidth = minWidth3,
      className = className3,
      packageName = packageName3,
      resizeMode = WidgetResizeMode(resizeMode3),
      updatePeriodMillis = updatePeriodMillis3,
      label = label3,
      preview = preview3)
  )

  val widgets: Seq[AppWidget] = Seq(
    AppWidget(
      userHashCode = userHashCodeOption1,
      autoAdvanceViewId = autoAdvanceViewId1,
      initialLayout = initialLayout1,
      minWidth = minWidth1,
      minHeight = minWidth1,
      minResizeWidth = minWidth1,
      minResizeHeight = minWidth1,
      className = className1,
      packageName = packageName1,
      resizeMode = WidgetResizeMode(resizeMode1),
      updatePeriodMillis = updatePeriodMillis1,
      label = label1,
      preview = preview1),
    AppWidget(
      userHashCode = userHashCodeOption2,
      autoAdvanceViewId = autoAdvanceViewId2,
      initialLayout = initialLayout2,
      minWidth = minWidth2,
      minHeight = minWidth2,
      minResizeWidth = minWidth2,
      minResizeHeight = minWidth2,
      className = className2,
      packageName = packageName2,
      resizeMode = WidgetResizeMode(resizeMode2),
      updatePeriodMillis = updatePeriodMillis2,
      label = label2,
      preview = preview2),
    AppWidget(
      userHashCode = userHashCodeOption3,
      autoAdvanceViewId = autoAdvanceViewId3,
      initialLayout = initialLayout3,
      minWidth = minWidth3,
      minHeight = minWidth3,
      minResizeWidth = minWidth3,
      minResizeHeight = minWidth3,
      className = className3,
      packageName = packageName3,
      resizeMode = WidgetResizeMode(resizeMode3),
      updatePeriodMillis = updatePeriodMillis3,
      label = label3,
      preview = preview3)
  )

  val appWithWidgets = widgets map { widget =>
    AppsWithWidgets(
      name = widget.packageName match {
        case `packageName1` => name1
        case `packageName2` => name2
        case `packageName3` => name3
        case _ => ""
      },
      packageName = widget.packageName,
      widgets = Seq(widget))
  }

  val call1 = Call(
      number = phoneNumber1,
      name = Some(contactName1),
      numberType = PhoneMobile,
      date = date1,
      callType = callType1)
  val call2 = Call(
      number = phoneNumber2,
      name = Some(contactName2),
      numberType = numberType2,
      date = date2,
      callType = callType2)
  val call3 =  Call(
      number = phoneNumber3,
      name = Some(contactName3),
      numberType = numberType3,
      date = date3,
      callType = callType3)

  val calls: Seq[Call] = Seq(call1, call2, call3)

  val callsContacts: Seq[Contact] = Seq(
   Contact(
      name = contactName1,
      lookupKey = lookupKey1,
      photoUri = photoUri1,
      hasPhone = false,
      favorite = false,
      info = None),
   Contact(
      name = contactName2,
      lookupKey = lookupKey2,
      photoUri = photoUri2,
      hasPhone = false,
      favorite = false,
      info = None),
   Contact(
      name = contactName3,
      lookupKey = lookupKey3,
      photoUri = photoUri3,
      hasPhone = false,
      favorite = false,
      info = None))

  val lastCallsContacts: Seq[LastCallsContact] = Seq(
    LastCallsContact(
      hasContact = true,
      number = phoneNumber1,
      title = contactName1,
      photoUri = Some(photoUri1),
      lookupKey = Some(lookupKey1),
      lastCallDate = date1,
      calls = Seq(call1)),
    LastCallsContact(
      hasContact = true,
      number = phoneNumber2,
      title = contactName2,
      photoUri = Some(photoUri2),
      lookupKey = Some(lookupKey2),
      lastCallDate = date2,
      calls = Seq(call2)),
    LastCallsContact(
      hasContact = true,
      number = phoneNumber3,
      title = contactName3,
      photoUri = Some(photoUri3),
      lookupKey = Some(lookupKey3),
      lastCallDate = date3,
      calls = Seq(call3)))

  val iterableCursorContact = new IterableCursor[Contact] {
    override def count(): Int = contacts.length

    override def moveToPosition(pos: Int): Contact = contacts(pos)

    override def close(): Unit = ()
  }

  val iterableContact = new IterableContacts(iterableCursorContact)

  val mockIterableCursor = new IterableCursor[RepositoryApp] {
    override def count(): Int = 0

    override def moveToPosition(pos: Int): RepositoryApp = javaNull

    override def close(): Unit = ()
  }

  val iterableCursorApps = new ServicesIterableApps(mockIterableCursor) {
    override def count(): Int = seqApplication.length

    override def moveToPosition(pos: Int): Application = seqApplication(pos)

    override def close(): Unit = ()
  }

  val iterableApps = new IterableApps(iterableCursorApps)

  val appsCounters = Seq(
    TermCounter("#", 4),
    TermCounter("B", 1),
    TermCounter("E", 6),
    TermCounter("F", 5),
    TermCounter("Z", 3))

  val categoryCounters = Seq(
    TermCounter("COMMUNICATION", 4),
    TermCounter("GAMES", 1),
    TermCounter("SOCIAL", 6),
    TermCounter("TOOLS", 5),
    TermCounter("WEATHER", 3))

  val contactsCounters = Seq(
    TermCounter("#", 4),
    TermCounter("B", 1),
    TermCounter("E", 6),
    TermCounter("F", 5),
    TermCounter("Z", 3))

  val installationAppsCounters = Seq(
    TermCounter("oneWeek", 4),
    TermCounter("twoWeeks", 1),
    TermCounter("oneMonth", 6),
    TermCounter("twoMonths", 5))

  val networks = 0 to 10 map (c => s"Networks $c")

}
