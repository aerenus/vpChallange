package com.erenus.vp.network.retrofit

import com.erenus.vp.network.models.*
import com.erenus.vp.utils.Constraints
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by eren.faikoglu on 6.01.2022.
 */
interface ApiInterface {
    @POST(Constraints.HANDSHAKE_URL)
    fun callHandshake(@Body param: HandshakeOutgoing) : Call<HandshakeIncoming>

    //todo bkz. todo 5
    @POST(Constraints.STOCKS_URL)
    fun callStocks(@Header(Constraints.AUTHORIZATION) authorization: String?, @Body param: StockOutgoing?) : Call<StocksModelIncoming>

    @POST(Constraints.STOCK_DETAIL_URL)
    fun callStockDetail(@Header(Constraints.AUTHORIZATION) authorization: String?, @Body param: DetailModelOutgoing?) : Call<DetailModel>
}