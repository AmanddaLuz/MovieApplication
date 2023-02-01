package com.amandaluz.core.util.dialog

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.amandaluz.core.R
import com.amandaluz.ui.customView.ConfirmDialog

fun Context.openDialogConnection(yes: () -> Unit, no: () -> Unit, manager: FragmentManager) {
    ConfirmDialog(
        getString(R.string.title_dialog),
        getString(R.string.message_dialog_button),
        getString(R.string.try_again_dialog_button),
        getString(R.string.cancel_dialog_button)
    ).apply {
        setListener {
            yes.invoke()
            dismiss()
        }
        setListenerCancel {
            no.invoke()
            dismiss()
        }
    }.show(manager, this.getString(R.string.tag_connection))
}

fun Context.openDialogNoConnection(yes: () -> Unit, no: () -> Unit, manager: FragmentManager) {
    ConfirmDialog(
        getString(R.string.title_dialog),
        getString(R.string.message_dialog_no_connection),
        "",
        getString(R.string.cancel_dialog_button)
    ).apply {
        setListener {
            yes.invoke()
            dismiss()
        }
        setListenerCancel {
            no.invoke()
            dismiss()
        }
    }.show(manager, getString(R.string.tag_connection))
}

fun Context.openDialogError(yes: () -> Unit, manager: FragmentManager) {
    ConfirmDialog(
        getString(R.string.title_dialog_error),
        getString(R.string.message_dialog_error),
        "",
        getString(R.string.close_dialog_button)
    ).apply {
        setListener {
            yes.invoke()
            dismiss()
        }
        setListenerCancel {
            dismiss()
        }
    }.show(manager, getString(R.string.tag_connection))
}

fun popularity() = "Popularidade: "
