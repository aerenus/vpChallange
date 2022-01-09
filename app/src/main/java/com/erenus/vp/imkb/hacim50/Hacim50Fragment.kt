package com.erenus.vp.imkb.hacim50

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
import com.erenus.vp.databinding.Hacim50FragmentBinding
import com.erenus.vp.network.models.StockOutgoing
import com.erenus.vp.network.models.StocksModelIncoming
import com.erenus.vp.utils.*

class Hacim50Fragment : Fragment() {

    companion object {
        fun newInstance() = Hacim50Fragment()
    }

    private lateinit var viewModel: Hacim50ViewModel
    private val prefRepository by lazy { PrefRepository(requireContext()) }
    private val cryptoUtil by lazy { CryptoUtil(requireContext()) }
    private lateinit var hacim50FragmentBinding: Hacim50FragmentBinding
    private val stockAdapter = StockAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[Hacim50ViewModel::class.java]
        hacim50FragmentBinding = Hacim50FragmentBinding.inflate(inflater, container, false)
        hacim50FragmentBinding.lifecycleOwner = this
        hacim50FragmentBinding.vm = viewModel
        return hacim50FragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[Hacim50ViewModel::class.java]

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
        if(cryptoUtil.encrypt("volume50") != null) {
            return StockOutgoing(cryptoUtil.encrypt("volume50")!!)
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
        hacim50FragmentBinding.rvStockList.layoutManager = LinearLayoutManager(context)
        hacim50FragmentBinding.rvStockList.adapter = stockAdapter

        hacim50FragmentBinding.etSearch.doOnTextChanged { text, _, _, _ ->
            stockAdapter.filter(text.toString())
        }

        hacim50FragmentBinding.swipeRefreshLayout.isEnabled = false

        /* hisseFragmentBinding.swipeRefreshLayout.setOnRefreshListener {
            getStocks(true)
        } */
    }
}