package com.erenus.vp.network.models

/**
 * Created by eren.faikoglu on 6.01.2022.
 */
data class StockModel(
    var id: Int? = null,
    var isDown: Boolean? = null,
    var isUp: Boolean? = null,
    var bid: Double? = null,
    var difference: Double? = null,
    var offer: Double? = null,
    var price: Double? = null,
    var volume: Double? = null,
    var symbol: String? = null,
    var isDecrypted: Boolean? = false
)