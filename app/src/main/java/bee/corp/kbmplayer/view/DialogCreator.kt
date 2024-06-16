package bee.corp.kbmplayer.view

import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.view.View
import androidx.appcompat.app.AlertDialog

class DialogCreator {
    companion object {
        fun createViewDialog(c: Context, resid: View?,
                             message: String, title: String,
                             positiveButtonText: String = "",
                             positiveButtonClickListener: DialogInterface.OnClickListener) : AlertDialog {
            val builder = AlertDialog.Builder(c)
            builder.setView(resid)
            builder.setTitle(title)
            builder.setMessage(message)
            if(positiveButtonText.isNotBlank()) {
                builder.setPositiveButton(positiveButtonText, positiveButtonClickListener)
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            return builder.create()
        }
        fun createSimpleDialog(c: Context, message: String, title: String,
                               positiveButtonText: String, positiveButtonClickListener: OnClickListener,
                               negativeButtonText: String, negativeButtonClickListener: OnClickListener?) : AlertDialog {
            val builder = AlertDialog.Builder(c)
            builder.setMessage(message)
            builder.setTitle(title)
            builder.setPositiveButton(positiveButtonText, positiveButtonClickListener)
            builder.setNegativeButton(negativeButtonText, negativeButtonClickListener)

            return builder.create()
        }
    }
}