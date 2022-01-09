package com.erenus.vp.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Created by eren.faikoglu on 6.01.2022.
 */
private var toast: Toast? = null

fun Context.toast(message: CharSequence?) {
    toast?.cancel()
    toast = message?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT) }?.apply { show() }
}

fun Context.longToast(message: CharSequence?) {
    toast?.cancel()
    toast = message?.let { Toast.makeText(this, it, Toast.LENGTH_LONG) }?.apply { show() }
}

fun Context.toast(@StringRes message: Int) {
    toast?.cancel()
    toast = Toast.makeText(this, message, Toast.LENGTH_SHORT).apply { show() }
}

fun Context.longToast(@StringRes message: Int) {
    toast?.cancel()
    toast = Toast.makeText(this, message, Toast.LENGTH_LONG).apply { show() }
}