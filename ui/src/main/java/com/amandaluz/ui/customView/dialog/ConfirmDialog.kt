package com.amandaluz.ui.customView.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.amandaluz.ui.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ConfirmDialog(
    private val title: String,
    private val message: String,
    private val textYes: String,
    private val textNo: String
) : DialogFragment() {

    private var yesListener: (() -> Unit )? = null
    private var noListener: (() -> Unit )? = null
    fun setListener(listener: () -> Unit){yesListener = listener}
    fun setListenerCancel(listener: () -> Unit){noListener = listener}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(textYes){ _, _ ->
                yesListener?.let {
                    it()
                }
            }
            .setNegativeButton(textNo){ _, _ ->
                noListener?.let {
                    it()
                }
            }
            .create()
}