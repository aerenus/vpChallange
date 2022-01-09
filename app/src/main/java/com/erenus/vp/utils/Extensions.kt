package com.erenus.vp.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Created by eren.faikoglu on 8.01.2022.
 */

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun prepareDecimalforUI(value: Double?, size: Int): String? {
    if(value != null) {
        return BigDecimal(value).setScale(size, RoundingMode.HALF_EVEN).toString()
    }
    return null
}