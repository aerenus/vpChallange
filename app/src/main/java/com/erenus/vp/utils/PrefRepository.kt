package com.erenus.vp.utils

import android.content.Context
import android.content.SharedPreferences
import com.erenus.vp.utils.Constraints.Companion.PREF_NAME

/**
 * Created by eren.faikoglu on 6.01.2022.
 */
class PrefRepository(val context: Context){
    private var pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun putString(key: String,value: String?) {
        pref.edit().putString(key, value).apply()
    }

    fun getString(key: String): String? {
        return pref.getString(key, null)
    }

    fun putInt(key: String, value: Int?) {
        if (value != null) {
            pref.edit().putInt(key, value).apply()
        }
    }

    fun getInt(key: String): Int {
        return pref.getInt(key, -1)
    }
}