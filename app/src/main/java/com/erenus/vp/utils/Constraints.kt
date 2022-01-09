package com.erenus.vp.utils

/**
 * Created by eren.faikoglu on 6.01.2022.
 */
class Constraints {
    companion object {
        const val BASE_URL = "https://erenus.net/api/"
        const val HANDSHAKE_URL = "handshake/start"
        const val STOCKS_URL = "stocks/list"
        const val STOCK_DETAIL_URL = "stocks/detail"

        const val PREF_NAME = "VP_PREFS"

        const val AES_KEY = "AES_KEY"
        const val AES_IV = "AES_IV"
        const val AUTHORIZATION = "X-VP-Authorization"

        const val DECIMAL_SIZE  = 2
        const val DECIMAL_ZERO = 0
        const val DECIMAL_ONE  = 2

        const val INTENT_EXTRA_STOCK_ID = "STOCKID"
    }
}
