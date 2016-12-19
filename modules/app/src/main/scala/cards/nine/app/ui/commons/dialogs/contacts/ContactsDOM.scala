package cards.nine.app.ui.commons.dialogs.contacts

import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import cards.nine.app.ui.commons.adapters.contacts.ContactsAdapter
import cards.nine.commons._
import com.fortysevendeg.ninecardslauncher.{TR, TypedFindView}
import macroid.ActivityContextWrapper

trait ContactsDOM { finder: TypedFindView =>

  val tagDialog = "contact-dialog"

  lazy val recycler = findView(TR.actions_recycler)

  def getAdapter: Option[ContactsAdapter] =
    Option(recycler.getAdapter) match {
      case Some(a: ContactsAdapter) => Some(a)
      case _                        => None
    }

  // TODO We should move this call to NavigationProcess #826
  def showDialog(dialog: DialogFragment)(
      implicit activityContextWrapper: ActivityContextWrapper): Unit = {
    activityContextWrapper.original.get match {
      case Some(activity: AppCompatActivity) =>
        val ft = activity.getSupportFragmentManager.beginTransaction()
        Option(activity.getSupportFragmentManager.findFragmentByTag(tagDialog)) foreach ft.remove
        ft.addToBackStack(javaNull)
        dialog.show(ft, tagDialog)
      case _ =>
    }
  }

}

trait ContactsUiListener {

  def loadContacts(): Unit

  def loadContactsByKeyword(keyword: String): Unit

  def showContact(lookupKey: String): Unit

}
