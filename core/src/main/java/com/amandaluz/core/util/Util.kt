package com.amandaluz.core.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.amandaluz.core.R
import com.amandaluz.ui.customView.ConfirmDialog

fun openNewTabWindow(urls: String, context: Context) {
    val uris = Uri.parse(urls)
    val intents = Intent(Intent.ACTION_VIEW, uris)
    val b = Bundle()
    b.putBoolean(context.getString(R.string.window_youtube), true)
    intents.putExtras(b)
    context.startActivity(intents)
}

fun openDialogConnection(yes: () -> Unit, no: () -> Unit, manager: FragmentManager) {
    ConfirmDialog(
        "Sem conexão",
        "Verifique sua internet e tente novamente!",
        "Tentar novamente",
        "Cancelar"
    ).apply {
        setListener {
            yes.invoke()
            dismiss()
        }
        setListenerCancel {
            no.invoke()
            dismiss()
        }
    }.show(manager, "Connection")
}

fun openDialogNoConnection(yes: () -> Unit, no: () -> Unit, manager: FragmentManager) {
    ConfirmDialog(
        "Sem conexão",
        "Não foi possível se conectar!",
        "",
        "Cancelar"
    ).apply {
        setListener {
            yes.invoke()
            dismiss()
        }
        setListenerCancel {
            no.invoke()
            dismiss()
        }
    }.show(manager, "Connection")
}

fun openDialogError(yes: () -> Unit, manager: FragmentManager) {
    ConfirmDialog(
        "Instabilidade",
        "Ocorreu um erro! Tente novamente em alguns instantes",
        "Tentar novamente",
        "Cancelar"
    ).apply {
        setListener {
            yes.invoke()
            dismiss()
        }
        setListenerCancel {
            dismiss()
        }
    }.show(manager, "Connection")
}

fun popularity() = "Popularidade: "

fun RecyclerView.animateList() {
    val animationController: LayoutAnimationController =
        AnimationUtils.loadLayoutAnimation(context, com.amandaluz.ui.R.anim.layout_animation)
    this.layoutAnimation = animationController
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}
