package com.erenus.vp.network.models

/**
 * Created by eren.faikoglu on 6.01.2022.
 */
data class StatusModel (
    var isSuccess: Boolean? = null,
    val error: ErrorModel? = null
)