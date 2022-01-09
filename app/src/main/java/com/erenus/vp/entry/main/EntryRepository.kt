package com.erenus.vp.entry.main

import androidx.lifecycle.MutableLiveData
import com.erenus.vp.network.models.HandshakeIncoming
import com.erenus.vp.network.models.HandshakeOutgoing
import com.erenus.vp.network.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Response

/**
 * Created by eren.faikoglu on 6.01.2022.
 */
class EntryRepository() {
    val handshake = MutableLiveData<HandshakeIncoming?>()

    fun getCallHandshake(deviceInfo: HandshakeOutgoing): MutableLiveData<HandshakeIncoming?> {
        val call = RetrofitClient.apiInterface.callHandshake(deviceInfo)

        call.enqueue(object: retrofit2.Callback<HandshakeIncoming> {
            override fun onFailure(call: Call<HandshakeIncoming>, t: Throwable) {
                handshake.postValue(null)
            }

            override fun onResponse(
                call: Call<HandshakeIncoming>,
                response: Response<HandshakeIncoming>
            ) {
                val data = response.body()
                if(data != null) {
                    handshake.value = data
                }
            }
        })
        return handshake
    }
}