package com.erenus.vp.network.models

/**
 * Created by eren.faikoglu on 6.01.2022.
 */
data class HandshakeIncoming(
    val aesKey: String? = null,
    val aesIV: String? = null,
    val authorization: String? = null,
    val lifetime: String? = null,
    val status: StatusModel? = null
)