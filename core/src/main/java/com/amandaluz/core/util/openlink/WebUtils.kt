package com.amandaluz.core.util.openlink

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.amandaluz.core.R

fun openNewTabWindow(urls: String, context: Context) {
    val uris = Uri.parse(urls)
    val intents = Intent(Intent.ACTION_VIEW, uris)
    val b = Bundle()
    b.putBoolean(context.getString(R.string.window_youtube), true)
    intents.putExtras(b)
    context.startActivity(intents)
}