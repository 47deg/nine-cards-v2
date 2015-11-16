package com.fortysevendeg.ninecardslauncher.process.collection

import com.fortysevendeg.ninecardslauncher.commons.contexts.ContextSupport
import com.fortysevendeg.ninecardslauncher.process.collection.models.NineCardIntentImplicits._
import com.fortysevendeg.ninecardslauncher.process.collection.models.NineCardsIntentExtras._
import com.fortysevendeg.ninecardslauncher.process.collection.models._
import com.fortysevendeg.ninecardslauncher.process.commons.types.NineCardCategory
import com.fortysevendeg.ninecardslauncher.process.types._
import com.fortysevendeg.ninecardslauncher.services.apps.models.Application
import com.fortysevendeg.ninecardslauncher.services.persistence.models.{Card => ServicesCard, Collection => ServicesCollection}
import com.fortysevendeg.ninecardslauncher.services.persistence.{AddCardRequest => ServicesAddCardRequest, AddCollectionRequest => ServicesAddCollectionRequest, UpdateCardRequest => ServicesUpdateCardRequest, UpdateCollectionRequest => ServicesUpdateCollectionRequest, _}
import com.fortysevendeg.ninecardslauncher.services.utils.ResourceUtils
import play.api.libs.json.Json

trait Conversions {

  val resourceUtils = new ResourceUtils

  def toCollectionSeq(servicesCollectionSeq: Seq[ServicesCollection]) = servicesCollectionSeq map toCollection

  def toCollection(servicesCollection: ServicesCollection) = Collection(
    id = servicesCollection.id,
    position = servicesCollection.position,
    name = servicesCollection.name,
    collectionType = CollectionType(servicesCollection.collectionType),
    icon = servicesCollection.icon,
    themedColorIndex = servicesCollection.themedColorIndex,
    appsCategory = servicesCollection.appsCategory flatMap (NineCardCategory(_)),
    constrains = servicesCollection.constrains,
    originalSharedCollectionId = servicesCollection.originalSharedCollectionId,
    sharedCollectionId = servicesCollection.sharedCollectionId,
    sharedCollectionSubscribed = servicesCollection.sharedCollectionSubscribed,
    cards = servicesCollection.cards map toCard)

  def toAddCollectionRequest(addCollectionRequest: AddCollectionRequest, position: Int) = ServicesAddCollectionRequest(
    position = position,
    name = addCollectionRequest.name,
    collectionType = addCollectionRequest.collectionType.name,
    icon = addCollectionRequest.icon,
    themedColorIndex = addCollectionRequest.themedColorIndex,
    appsCategory = addCollectionRequest.appsCategory map(_.name),
    constrains = addCollectionRequest.constrains,
    originalSharedCollectionId = addCollectionRequest.originalSharedCollectionId,
    sharedCollectionId = addCollectionRequest.sharedCollectionId,
    sharedCollectionSubscribed = addCollectionRequest.sharedCollectionSubscribed,
    cards = Seq())

  def toFindCollectionByIdRequest(collectionId: Int) = FindCollectionByIdRequest(
    id = collectionId)

  def toServicesUpdateCollectionRequest(collection: Collection) = ServicesUpdateCollectionRequest(
    id = collection.id,
    position = collection.position,
    name = collection.name,
    collectionType = collection.collectionType.name,
    icon = collection.icon,
    themedColorIndex = collection.themedColorIndex,
    appsCategory = collection.appsCategory map(_.name),
    constrains = collection.constrains,
    originalSharedCollectionId = collection.originalSharedCollectionId,
    sharedCollectionId = collection.sharedCollectionId,
    sharedCollectionSubscribed = Option(collection.sharedCollectionSubscribed),
    cards = collection.cards map toServicesCard)

  def toNewPositionCollection(collection: Collection, newPosition: Int) =  Collection(
    id = collection.id,
    position = newPosition,
    name = collection.name,
    collectionType = collection.collectionType,
    icon = collection.icon,
    themedColorIndex = collection.themedColorIndex,
    appsCategory = collection.appsCategory,
    constrains = collection.constrains,
    originalSharedCollectionId = collection.originalSharedCollectionId,
    sharedCollectionId = collection.sharedCollectionId,
    sharedCollectionSubscribed = collection.sharedCollectionSubscribed,
    cards = collection.cards)

  def toUpdatedCollection(collection: Collection, editCollectionRequest: EditCollectionRequest) =  Collection(
    id = collection.id,
    position = collection.position,
    name = editCollectionRequest.name,
    collectionType = collection.collectionType,
    icon = editCollectionRequest.icon,
    themedColorIndex = editCollectionRequest.themedColorIndex,
    appsCategory = editCollectionRequest.appsCategory,
    constrains = collection.constrains,
    originalSharedCollectionId = collection.originalSharedCollectionId,
    sharedCollectionId = collection.sharedCollectionId,
    sharedCollectionSubscribed = collection.sharedCollectionSubscribed,
    cards = collection.cards)

  def toFetchCollectionByPositionRequest(pos: Int) = FetchCollectionByPositionRequest(
    position = pos)

  def toCardSeq(servicesCardSeq: Seq[ServicesCard]) = servicesCardSeq map toCard

  def toCard(servicesCard: ServicesCard) = Card(
    id = servicesCard.id,
    position = servicesCard.position,
    micros = servicesCard.micros,
    term = servicesCard.term,
    packageName = servicesCard.packageName,
    cardType = CardType(servicesCard.cardType),
    intent = jsonToNineCardIntent(servicesCard.intent),
    imagePath = servicesCard.imagePath,
    starRating = servicesCard.starRating,
    numDownloads = servicesCard.numDownloads,
    notification = servicesCard.notification)

  def toServicesCard(card: Card) = ServicesCard(
    id = card.id,
    position = card.position,
    micros = card.micros,
    term = card.term,
    packageName = card.packageName,
    cardType = card.cardType.name,
    intent = nineCardIntentToJson(card.intent),
    imagePath = card.imagePath,
    starRating = card.starRating,
    numDownloads = card.numDownloads,
    notification = card.notification)

  def toAddCardRequestSeq(items: Seq[UnformedApp]): Seq[ServicesAddCardRequest] =
    items.zipWithIndex map (zipped => toAddCardRequestFromUnformedItems(zipped._1, zipped._2))

  def toAddCardRequestFromUnformedItems(item: UnformedApp, position: Int) = ServicesAddCardRequest(
    position = position,
    term = item.name,
    packageName = Option(item.packageName),
    cardType = AppCardType.name,
    intent = nineCardIntentToJson(toNineCardIntent(item)),
    imagePath = item.imagePath)

  def toFetchCardsByCollectionRequest(collectionRequestId: Int) = FetchCardsByCollectionRequest(
    collectionId = collectionRequestId)

  def toAddCardRequest(collectionId: Int, addCardRequest: AddCardRequest, position: Int) = ServicesAddCardRequest (
    collectionId = Option(collectionId),
    position = position,
    term = addCardRequest.term,
    packageName = addCardRequest.packageName,
    cardType = addCardRequest.cardType.name,
    intent = nineCardIntentToJson(addCardRequest.intent),
    imagePath = addCardRequest.imagePath)

  def toFindCardByIdRequest(cardId: Int) = FindCardByIdRequest(
    id = cardId)

  def toServicesUpdateCardRequest(card: Card) = ServicesUpdateCardRequest(
    id = card.id,
    position = card.position,
    micros = card.micros,
    term = card.term,
    packageName = card.packageName,
    cardType = card.cardType.name,
    intent = nineCardIntentToJson(card.intent),
    imagePath = card.imagePath,
    starRating = card.starRating,
    numDownloads = card.numDownloads,
    notification = card.notification)

  def toInstalledApp(cards: Seq[ServicesCard], app: Application)(implicit contextSupport: ContextSupport): Seq[ServicesCard] = {
    val intent = toNineCardIntent(app)
    cards map (_.copy(
      term = app.name,
      cardType = AppCardType.name,
      imagePath = resourceUtils.getPathPackage(app.packageName, app.className),
      intent = nineCardIntentToJson(intent)
    ))
  }

  def toNewPositionCard(card: Card, newPosition: Int) = Card(
    id = card.id,
    position = card.position,
    micros = card.micros,
    term = card.term,
    packageName = card.packageName,
    cardType = card.cardType,
    intent = card.intent,
    imagePath = card.imagePath,
    starRating = card.starRating,
    numDownloads = card.numDownloads,
    notification = card.notification)

  def toUpdatedCard(card: Card, name: String) = Card(
    id = card.id,
    position = card.position,
    micros = card.micros,
    term = name,
    packageName = card.packageName,
    cardType = card.cardType,
    intent = card.intent,
    imagePath = card.imagePath,
    starRating = card.starRating,
    numDownloads = card.numDownloads,
    notification = card.notification)

  def toNineCardIntent(item: UnformedApp) = {
    val intent = NineCardIntent(NineCardIntentExtras(
      package_name = Option(item.packageName),
      class_name = Option(item.className)))
    intent.setAction(openApp)
    intent.setClassName(item.packageName, item.className)
    intent
  }

  def toNineCardIntent(app: Application) = {
    val intent = NineCardIntent(NineCardIntentExtras(
      package_name = Option(app.packageName),
      class_name = Option(app.className)))
    intent.setAction(openApp)
    intent.setClassName(app.packageName, app.className)
    intent
  }

  def toAddCardRequestByContacts(items: Seq[UnformedContact]): Seq[ServicesAddCardRequest] =
    items.zipWithIndex map (zipped => toAddCardRequestByContact(zipped._1, zipped._2))

  def toAddCardRequestByContact(item: UnformedContact, position: Int) = {
    val (intent: NineCardIntent, cardType: String) = toNineCardIntent(item)
    ServicesAddCardRequest(
      position = position,
      term = item.name,
      packageName = None,
      cardType = cardType,
      intent = nineCardIntentToJson(intent),
      imagePath = item.photoUri)
  }

  def toNineCardIntent(item: UnformedContact): (NineCardIntent, String) = item match {
    case UnformedContact(_, _, _, Some(info)) if info.phones.nonEmpty =>
      val phone = info.phones.headOption map (_.number)
      val intent = NineCardIntent(NineCardIntentExtras(tel = phone))
      intent.setAction(openPhone)
      (intent, PhoneCardType.name)
    case UnformedContact(_, _, _, Some(info)) if info.emails.nonEmpty =>
      val address = info.emails.headOption map (_.address)
      val intent = NineCardIntent(NineCardIntentExtras(email = address))
      intent.setAction(openEmail)
      (intent, EmailCardType.name)
    case _ => // TODO 9C-234 - We should create a new action for open contact and use it here
      val intent = NineCardIntent(NineCardIntentExtras())
      (intent, AppCardType.name)
  }

  def jsonToNineCardIntent(json: String) = Json.parse(json).as[NineCardIntent]

  def nineCardIntentToJson(intent: NineCardIntent) = Json.toJson(intent).toString()

}
