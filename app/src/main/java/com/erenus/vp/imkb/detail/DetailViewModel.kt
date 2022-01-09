package com.erenus.vp.imkb.detail

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erenus.vp.network.models.DetailModel
import com.erenus.vp.network.models.DetailModelOutgoing

/**
 * Created by eren.faikoglu on 9.01.2022.
 */
class DetailViewModel: ViewModel() {
    var stockDetailResult: MutableLiveData<DetailModel?>? = null
    var iRepository = DetailRepository()
    var isLoading = ObservableBoolean()

    fun getStockDetail(auth: String?, id: DetailModelOutgoing?): MutableLiveData<DetailModel?>? {
        stockDetailResult = iRepository.getCallStockDetail(auth, id)
        return stockDetailResult
    }

    fun loading(loading: Boolean) {
        isLoading.set(loading)
    }
}