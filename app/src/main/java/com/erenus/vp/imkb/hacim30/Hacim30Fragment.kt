package com.erenus.vp.imkb.hacim30

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.erenus.vp.R
import com.erenus.vp.adapter.StockAdapter
import com.erenus.vp.databinding.Hacim30FragmentBinding
import com.erenus.vp.network.models.StockOutgoing
import com.erenus.vp.network.models.StocksModelIncoming
import com.erenus.vp.utils.*

class Hacim30Fragment : Fragment() {

    companion object {
        fun newInstance() = Hacim30Fragment()
    }

    private lateinit var viewModel: Hacim30ViewModel
    private val prefRepository by lazy { PrefRepository(requireContext()) }
    private val cryptoUtil by lazy { CryptoUtil(requireContext()) }
    private lateinit var hacim30FragmentBinding: Hacim30FragmentBinding
    private val stockAdapter = StockAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[Hacim30ViewModel::class.java]
        hacim30FragmentBinding = Hacim30FragmentBinding.inflate(inflater, container, false)
        hacim30FragmentBinding.lifecycleOwner = this
        hacim30FragmentBinding.vm = viewModel
        return hacim30FragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[Hacim30ViewModel::class.java]

        prepareUI()
        getStocks(false)
    }

    fun getStocks(refresh: Boolean) {
        viewModel.loading(true)
        viewModel.getStocks(prefRepository.getString(Constraints.AUTHORIZATION), prepareStockOutgoing())?.observe(this, {
            when {
                it == null -> {
                    viewModel.loading(false)
                    notificationUtil(R.string.err_UzakSunucuBaglantisiYok, context!!)
                }
                it.status?.isSuccess == false -> {
                    viewModel.loading(false)
                    context!!.longToast(it.status.error?.message)
                }
                else -> {
                    prepareStocks(it, refresh)
                    viewModel.loading(false)
                }
            }
        })
    }

    fun prepareStockOutgoing(): StockOutgoing? {
        if(cryptoUtil.encrypt("volume30") != null) {
            return StockOutgoing(cryptoUtil.encrypt("volume30")!!)
        }
        return null
    }

    fun prepareStocks(stocks: StocksModelIncoming, refresh: Boolean) {
        if(stocks.stocks != null && stocks.stocks.size > 0) {
            if(!refresh) {
                for(i in 0 until stocks.stocks.size) {
                    if(!stocks.stocks[i].isDecrypted!!) {
                        stocks.stocks[i].symbol = stocks.stocks[i].symbol?.let { cryptoUtil.decrypt(it) }
                        stocks.stocks[i].isDecrypted = true
                    }
                }
            }
            stockAdapter.updateStocks(stocks.stocks)
        }
    }

    fun prepareUI() {
        hacim30FragmentBinding.rvStockList.layoutManager = LinearLayoutManager(context)
        hacim30FragmentBinding.rvStockList.adapter = stockAdapter

        hacim30FragmentBinding.etSearch.doOnTextChanged { text, _, _, _ ->
            stockAdapter.filter(text.toString())
        }

        hacim30FragmentBinding.swipeRefreshLayout.isEnabled = false

        /* hisseFragmentBinding.swipeRefreshLayout.setOnRefreshListener {
            getStocks(true)
        } */
    }
}