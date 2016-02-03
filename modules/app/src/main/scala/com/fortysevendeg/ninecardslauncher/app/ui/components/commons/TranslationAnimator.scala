package com.fortysevendeg.ninecardslauncher.app.ui.components.commons

import android.animation.{Animator, AnimatorListenerAdapter, ValueAnimator, ObjectAnimator}
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.ninecardslauncher2.R
import macroid.{Snail, ContextWrapper, Ui}
import macroid.FullDsl._

import scala.concurrent.Promise

class TranslationAnimator(
  translation: Translation = NoTranslation,
  update: (Float) => Ui[_])(implicit context: ContextWrapper) {

  val duration = resGetInteger(R.integer.anim_duration_normal)

  private[this] val animator: ValueAnimator = translation match {
    case NoTranslation => new ValueAnimator
    case _ =>
      val objectAnimator = new ObjectAnimator
      objectAnimator.setPropertyName(translation.name)
      objectAnimator
  }
  animator.setInterpolator(new DecelerateInterpolator())
  animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
    override def onAnimationUpdate(value: ValueAnimator) = runUi(update(value.getAnimatedValue.asInstanceOf[Float]))
  })

  def move(
    from: Float,
    to: Float,
    duration: Int = duration,
    attachTarget: Boolean = false): Snail[View] = Snail[View] { view =>
    val promise = Promise[Unit]()
    animator.removeAllListeners()
    animator.addListener(new AnimatorListenerAdapter() {
      override def onAnimationEnd(animation: Animator) = {
        super.onAnimationEnd(animation)
        promise.success()
      }
    })
    if (attachTarget) animator.setTarget(view)
    animator.setFloatValues(from, to)
    animator.setDuration(duration)
    animator.start()
    promise.future
  }

  def cancel(): Unit = animator.cancel()

  def isRunning: Boolean = animator.isRunning

}

sealed trait Translation {
  val name: String
}

case object TranslationX extends Translation {
  override val name: String = "translationX"
}

case object TranslationY extends Translation {
  override val name: String = "translationY"
}

case object NoTranslation extends Translation {
  override val name: String = ""
}