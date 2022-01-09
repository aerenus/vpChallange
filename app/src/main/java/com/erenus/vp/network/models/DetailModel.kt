package com.erenus.vp.network.models

/**
 * Created by eren.faikoglu on 9.01.2022.
 */
data class DetailModel (
    val isDown: Boolean?,
    val isUp: Boolean?,
    val bid: Double?,
    val change: Double?,
    val count: Double?,
    val difference: Double?,
    val offer: Double?,
    val highest: Double?,
    val lowest: Double?,
    val maximum: Double?,
    val minimum: Double?,
    val price: Double?,
    val volume: Double?,
    val symbol: String?,
    val graphicData: ArrayList<DetailGraphicDataModel>?,
    val status: StatusModel?
)