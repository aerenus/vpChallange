package com.erenus.vp.network.models

import com.squareup.moshi.Json
import java.io.Serializable

/**
 * Created by eren.faikoglu on 6.01.2022.
 */
data class StocksModelIncoming (
    val stocks: ArrayList<StockModel>? = null,
    val status: StatusModel? = null
): Serializable