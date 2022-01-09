package com.erenus.vp.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import java.lang.Exception
import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by eren.faikoglu on 7.01.2022.
 */
class CryptoUtil(val context: Context) {
    var pref: SharedPreferences = context.getSharedPreferences(Constraints.PREF_NAME, Context.MODE_PRIVATE)

    fun encrypt(message: String): String? {
        try {
            val srcBuff = message.toByteArray(charset("UTF8"))
            val skeySpec = SecretKeySpec(Base64.decode(pref.getString(Constraints.AES_KEY, null)!!, Base64.DEFAULT), "AES")
            val ivSpec = IvParameterSpec(Base64.decode(pref.getString(Constraints.AES_IV, null)!!, Base64.DEFAULT))
            val ecipher: Cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec)
            val dstBuff: ByteArray = ecipher.doFinal(srcBuff)
            return Base64.encodeToString(dstBuff, Base64.DEFAULT)
        } catch (ex: Exception) {
            context.longToast(ex.localizedMessage)
        }
        return null
    }

    fun decrypt(encrypted: String): String? {
        try {
            val skeySpec = SecretKeySpec(Base64.decode(pref.getString(Constraints.AES_KEY, null)!!, Base64.DEFAULT), "AES")
            val ivSpec = IvParameterSpec(Base64.decode(pref.getString(Constraints.AES_IV, null)!!, Base64.DEFAULT))
            val ecipher: Cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            ecipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec)
            val raw: ByteArray = Base64.decode(encrypted, Base64.DEFAULT)
            val originalBytes: ByteArray = ecipher.doFinal(raw)
            return String(originalBytes, Charset.forName("UTF-8"))
        } catch (ex: Exception) {
            context.longToast(ex.localizedMessage)
        }
        return null
    }
}