package com.fortysevendeg.ninecardslauncher.services.persistence.conversions

import com.fortysevendeg.ninecardslauncher.repository.model.{Card => RepoCard, CardData => RepoCardData}
import com.fortysevendeg.ninecardslauncher.services.api.models.NineCardIntent
import com.fortysevendeg.ninecardslauncher.services.api.models.NineCardIntentImplicits._
import com.fortysevendeg.ninecardslauncher.services.persistence._
import com.fortysevendeg.ninecardslauncher.services.persistence.models.Card
import play.api.libs.json.Json

trait CardConversions {

  def toCard(card: RepoCard) = {
    val intent = Json.parse(card.data.intent).as[NineCardIntent]
    Card(
      id = card.id,
      position = card.data.position,
      micros = card.data.micros,
      term = card.data.term,
      packageName = card.data.packageName,
      cardType = card.data.cardType,
      intent = intent,
      imagePath = card.data.imagePath,
      starRating = card.data.starRating,
      numDownloads = card.data.numDownloads,
      notification = card.data.notification)
  }

  def toRepositoryCard(card: Card) =
    RepoCard(
      id = card.id,
      data = RepoCardData(
        position = card.position,
        micros = card.micros,
        term = card.term,
        packageName = card.packageName,
        cardType = card.cardType,
        intent = Json.toJson(card.intent).toString(),
        imagePath = card.imagePath,
        starRating = card.starRating,
        numDownloads = card.numDownloads,
        notification = card.notification
      )
    )

  def toRepositoryCard(request: UpdateCardRequest) =
    RepoCard(
      id = request.id,
      data = RepoCardData(
        position = request.position,
        micros = request.micros,
        term = request.term,
        packageName = request.packageName,
        cardType = request.cardType,
        intent = request.intent,
        imagePath = request.imagePath,
        starRating = request.starRating,
        numDownloads = request.numDownloads,
        notification = request.notification
      )
    )

  def toRepositoryCardData(cardItem: CardItem) =
    RepoCardData(
      position = cardItem.position,
      term = cardItem.term,
      cardType = cardItem.cardType,
      micros = cardItem.micros,
      packageName = cardItem.packageName,
      intent = cardItem.intent,
      imagePath = cardItem.imagePath,
      starRating = cardItem.starRating,
      numDownloads = cardItem.numDownloads,
      notification = cardItem.notification
    )
}
