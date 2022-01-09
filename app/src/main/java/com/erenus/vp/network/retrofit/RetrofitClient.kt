package com.erenus.vp.network.retrofit

import android.app.Application
import androidx.core.content.ContentProviderCompat.requireContext
import com.erenus.vp.BuildConfig
import com.erenus.vp.utils.Constraints
import com.erenus.vp.utils.PrefRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by eren.faikoglu on 6.01.2022.
 */
object RetrofitClient {

    val retrofitClient: Retrofit.Builder by lazy {

        val levelType: HttpLoggingInterceptor.Level
        if (BuildConfig.BUILD_TYPE.contentEquals("debug"))
            levelType = HttpLoggingInterceptor.Level.BODY else levelType = HttpLoggingInterceptor.Level.NONE

        val logging = HttpLoggingInterceptor()
        logging.setLevel(levelType)

        val okhttpClient = OkHttpClient.Builder()
        //todo 5 normalde auth header'ı burdan bu şekilde göndermem lazım
        //shared pref okuyabilmem için de burada injection kullanmam lazım, basit yapı karmaşıklaşır
        /*
        okhttpClient.addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader(Constraints.AUTHORIZATION, "")
                .build()
            chain.proceed(newRequest)
        }
        */
        okhttpClient.addInterceptor(logging)

        Retrofit.Builder()
            .baseUrl(Constraints.BASE_URL)
            .client(okhttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiInterface: ApiInterface by lazy {
        retrofitClient
            .build()
            .create(ApiInterface::class.java)
    }
}