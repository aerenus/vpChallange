package com.erenus.vp.imkb.hacim100

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
import com.erenus.vp.databinding.Hacim100FragmentBinding
import com.erenus.vp.network.models.StockOutgoing
import com.erenus.vp.network.models.StocksModelIncoming
import com.erenus.vp.utils.*

class Hacim100Fragment : Fragment() {

    companion object {
        fun newInstance() = Hacim100Fragment()
    }

    private lateinit var viewModel: Hacim100ViewModel
    private val prefRepository by lazy { PrefRepository(requireContext()) }
    private val cryptoUtil by lazy { CryptoUtil(requireContext()) }
    private lateinit var hacim100FragmentBinding: Hacim100FragmentBinding
    private val stockAdapter = StockAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[Hacim100ViewModel::class.java]
        hacim100FragmentBinding = Hacim100FragmentBinding.inflate(inflater, container, false)
        hacim100FragmentBinding.lifecycleOwner = this
        hacim100FragmentBinding.vm = viewModel
        return hacim100FragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[Hacim100ViewModel::class.java]

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
        if(cryptoUtil.encrypt("volume100") != null) {
            return StockOutgoing(cryptoUtil.encrypt("volume100")!!)
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
        hacim100FragmentBinding.rvStockList.layoutManager = LinearLayoutManager(context)
        hacim100FragmentBinding.rvStockList.adapter = stockAdapter

        hacim100FragmentBinding.etSearch.doOnTextChanged { text, _, _, _ ->
            stockAdapter.filter(text.toString())
        }

        hacim100FragmentBinding.swipeRefreshLayout.isEnabled = false

        /* hisseFragmentBinding.swipeRefreshLayout.setOnRefreshListener {
            getStocks(true)
        } */
    }
}