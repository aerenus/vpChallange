package com.erenus.vp.imkb

import androidx.lifecycle.MutableLiveData
import com.erenus.vp.network.models.StockOutgoing
import com.erenus.vp.network.models.StocksModelIncoming
import com.erenus.vp.network.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Response

/**
 * Created by eren.faikoglu on 6.01.2022.
 */
class MainRepository {
    var stocks = MutableLiveData<StocksModelIncoming?>()

    fun getCallStocks(auth: String?, period: StockOutgoing?): MutableLiveData<StocksModelIncoming?> {
        val call = RetrofitClient.apiInterface.callStocks(auth, period)

        call.enqueue(object: retrofit2.Callback<StocksModelIncoming> {
            override fun onFailure(call: Call<StocksModelIncoming>, t: Throwable) {
                stocks.postValue(null)
            }

            override fun onResponse(
                call: Call<StocksModelIncoming>,
                response: Response<StocksModelIncoming>
            ) {
                val data = response.body()
                if(data != null) {
                    stocks.value = data
                }
            }
        })
        return stocks
    }

}