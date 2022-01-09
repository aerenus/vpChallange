package com.erenus.vp.utils

import android.app.AlertDialog
import android.content.Context
import androidx.annotation.StringRes
import android.content.DialogInterface
import com.erenus.vp.R


/**
 * Created by eren.faikoglu on 6.01.2022.
 */

fun notificationUtil(@StringRes message: Int, context: Context) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(context.resources.getString(R.string.uyari))
    builder.setMessage(message)

    builder.setNegativeButton(context.resources.getString(R.string.tamam)) { _, _ ->
    }
    builder.setCancelable(false)
    builder.show()
}

fun notificationUtilDirect(message: String, context: Context) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(context.resources.getString(R.string.uyari))
    builder.setMessage(message)

    builder.setNegativeButton(context.resources.getString(R.string.tamam)) { _, _ ->
    }
    builder.setCancelable(false)
    builder.show()
}