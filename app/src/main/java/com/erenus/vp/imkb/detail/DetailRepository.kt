package com.erenus.vp.imkb.detail

import androidx.lifecycle.MutableLiveData
import com.erenus.vp.network.models.*
import com.erenus.vp.network.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Response

/**
 * Created by eren.faikoglu on 9.01.2022.
 */

class DetailRepository {
    var stockDetail = MutableLiveData<DetailModel?>()

    fun getCallStockDetail(auth: String?, id: DetailModelOutgoing?): MutableLiveData<DetailModel?> {
        val call = RetrofitClient.apiInterface.callStockDetail(auth, id)

        call.enqueue(object: retrofit2.Callback<DetailModel> {
            override fun onFailure(call: Call<DetailModel>, t: Throwable) {
                stockDetail.postValue(null)
            }

            override fun onResponse(
                call: Call<DetailModel>,
                response: Response<DetailModel>
            ) {
                val data = response.body()
                if(data != null) {
                    stockDetail.value = data
                }
            }
        })
        return stockDetail
    }
}