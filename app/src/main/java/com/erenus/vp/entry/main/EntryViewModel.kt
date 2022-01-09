package com.erenus.vp.entry.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erenus.vp.network.models.HandshakeIncoming
import com.erenus.vp.network.models.HandshakeOutgoing

class EntryViewModel : ViewModel() {

    var handshakeResult: MutableLiveData<HandshakeIncoming?>? = null
    var iRepository = EntryRepository()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun getHandshake(deviceInfo: HandshakeOutgoing): MutableLiveData<HandshakeIncoming?>? {
        handshakeResult = iRepository.getCallHandshake(deviceInfo)
        return handshakeResult
    }

    fun setLoading(loading: Boolean) {
        isLoading.value = loading
    }
}