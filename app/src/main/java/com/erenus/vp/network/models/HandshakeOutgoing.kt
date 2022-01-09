package com.erenus.vp.network.models

/**
 * Created by eren.faikoglu on 6.01.2022.
 */
data class HandshakeOutgoing (
    var deviceId: String? = null,
    var systemVersion: String? = null,
    var platformName: String? = null,
    var deviceModel: String? = null,
    var manifacturer: String? = null
)