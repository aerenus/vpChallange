package com.erenus.vp.imkb.hisse

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
import com.erenus.vp.databinding.HisseFragmentBinding
import com.erenus.vp.network.models.StockModel
import com.erenus.vp.network.models.StockOutgoing
import com.erenus.vp.network.models.StocksModelIncoming
import com.erenus.vp.utils.*

class HisseFragment : Fragment() {

    companion object {
        fun newInstance() = HisseFragment()
    }

    private lateinit var viewModel: HisseViewModel
    private val prefRepository by lazy { PrefRepository(requireContext()) }
    private val cryptoUtil by lazy { CryptoUtil(requireContext()) }
    private lateinit var hisseFragmentBinding: HisseFragmentBinding
    private val stockAdapter = StockAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[HisseViewModel::class.java]
        hisseFragmentBinding = HisseFragmentBinding.inflate(inflater, container, false)
        hisseFragmentBinding.lifecycleOwner = this
        hisseFragmentBinding.vm = viewModel
        return hisseFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        if(cryptoUtil.encrypt("all") != null) {
            return StockOutgoing(cryptoUtil.encrypt("all")!!)
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
        hisseFragmentBinding.rvStockList.layoutManager = LinearLayoutManager(context)
        hisseFragmentBinding.rvStockList.adapter = stockAdapter

        hisseFragmentBinding.etSearch.doOnTextChanged { text, _, _, _ ->
            stockAdapter.filter(text.toString())
        }

        hisseFragmentBinding.swipeRefreshLayout.isEnabled = false


        /* hisseFragmentBinding.swipeRefreshLayout.setOnRefreshListener {
            getStocks(true)
        } */
    }


}