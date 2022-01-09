package com.erenus.vp.imkb.yukselenler

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erenus.vp.imkb.MainRepository
import com.erenus.vp.network.models.StockOutgoing
import com.erenus.vp.network.models.StocksModelIncoming

class YukselenlerViewModel : ViewModel() {
    var stocksResult: MutableLiveData<StocksModelIncoming?>? = null
    var iRepository = MainRepository()
    var isLoading = ObservableBoolean()

    fun getStocks(auth: String?, period: StockOutgoing?): MutableLiveData<StocksModelIncoming?>? {
        stocksResult = iRepository.getCallStocks(auth, period)
        return stocksResult
    }

    fun loading(loading: Boolean) {
        isLoading.set(loading)
    }
}