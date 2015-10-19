package com.fortysevendeg.ninecardslauncher.app.ui.collections.actions.recommendations

import android.support.v7.widget.RecyclerView
import android.view.{View, ViewGroup}
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.ExtraTweaks._
import com.fortysevendeg.ninecardslauncher.app.ui.commons.{LauncherExecutor, UiContext}
import com.fortysevendeg.ninecardslauncher.app.ui.commons.actions.{BaseActionFragment, Styles}
import com.fortysevendeg.ninecardslauncher.app.ui.commons.AsyncImageTweaks._
import com.fortysevendeg.ninecardslauncher.process.recommendations.models.RecommendedApp
import com.fortysevendeg.ninecardslauncher2.{R, TR, TypedFindView}
import macroid.FullDsl._
import macroid.{ActivityContextWrapper, Ui}

trait RecommendationsComposer
  extends Styles {

  self: TypedFindView with BaseActionFragment =>

  lazy val recycler = Option(findView(TR.actions_recycler))

  def initUi: Ui[_] =
    (toolbar <~
      tbTitle(R.string.recommendations) <~
      toolbarStyle(colorPrimary) <~
      tbNavigationOnClickListener((_) => unreveal())) ~
      (recycler <~ recyclerStyle)

  def showLoading: Ui[_] = (loading <~ vVisible) ~ (recycler <~ vGone)

  def showGeneralError: Ui[_] = rootContent <~ uiSnackbarShort(R.string.contactUsError)

  def addRecommendations(recommendations: Seq[RecommendedApp])(implicit uiContext: UiContext[_]) = {
    val adapter = new RecommendationsAdapter(recommendations)
    (recycler <~
      vVisible <~
      rvLayoutManager(adapter.getLayoutManager) <~
      rvAdapter(adapter)) ~
      (loading <~ vGone)
  }

}

case class ViewHolderRecommendationsLayoutAdapter(content: ViewGroup)(implicit context: ActivityContextWrapper)
  extends RecyclerView.ViewHolder(content)
  with TypedFindView
  with LauncherExecutor{

  lazy val icon = Option(findView(TR.recommendation_item_icon))

  lazy val name = Option(findView(TR.recommendation_item_name))

  lazy val downloads = Option(findView(TR.recommendation_item_downloads))

  lazy val tag = Option(findView(TR.recommendation_item_tag))

  lazy val screenshots = Seq(
    Option(findView(TR.recommendation_item_screenshot1)),
    Option(findView(TR.recommendation_item_screenshot2)),
    Option(findView(TR.recommendation_item_screenshot3)))

  lazy val description = Option(findView(TR.recommendation_item_description))

  lazy val installNow = Option(findView(TR.recommendation_item_install_now))

  def bind(recommendedApp: RecommendedApp, position: Int)(implicit uiContext: UiContext[_]): Ui[_] = {
    val screensUi: Seq[Ui[_]] = (screenshots zip recommendedApp.screenshots) map {
      case (view, screenshot) => view <~ ivUri(screenshot)
    }
    (icon <~ ivUri(recommendedApp.icon getOrElse "")) ~ // If icon don't exist ivUri will solve the problem
      (name <~ tvText(recommendedApp.title)) ~
      (downloads <~ tvText(recommendedApp.downloads)) ~
      (description <~ (recommendedApp.description map (d => tvText(d) + vVisible) getOrElse vGone)) ~
      (tag <~ tvText(if (recommendedApp.free) resGetString(R.string.free) else "")) ~
      (content <~ vIntTag(position)) ~
      Ui.sequence(screensUi: _*) ~
      (installNow <~ On.click (Ui(launchGooglePlay(recommendedApp.packageName))))
  }

  override def findViewById(id: Int): View = content.findViewById(id)

}