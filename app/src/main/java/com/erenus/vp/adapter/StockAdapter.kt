package com.erenus.vp.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.erenus.vp.R
import com.erenus.vp.imkb.MainActivity
import com.erenus.vp.imkb.detail.DetailActivity
import com.erenus.vp.network.models.StockModel
import com.erenus.vp.utils.Constraints
import com.erenus.vp.utils.inflate
import com.erenus.vp.utils.prepareDecimalforUI
import kotlinx.android.synthetic.main.stock_row.view.*
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Created by eren.faikoglu on 6.01.2022.
 */

class StockAdapter(private val stockList: ArrayList<StockModel>): RecyclerView.Adapter<StockAdapter.StockHolder>() {

    var stockListCopy: ArrayList<StockModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockHolder {
        val inflatedView = parent.inflate(R.layout.stock_row, false)
        return StockHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return stockList.size
    }

    override fun onBindViewHolder(holder: StockHolder, position: Int) {
        val stock = stockList[position]
        holder.bindStock(stock)
    }

    class StockHolder(private val view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        private var stock: StockModel? = null

        fun bindStock(stock: StockModel) {
            this.stock = stock
            view.setBackgroundColor(if (position % 2 == 0) Color.WHITE else ContextCompat.getColor(view.context, R.color.softGray))
            view.sembol.text = stock.symbol
            view.fiyat.text = prepareDecimalforUI(stock.price, Constraints.DECIMAL_SIZE)
            view.fark.text = prepareDecimalforUI(stock.difference, Constraints.DECIMAL_SIZE)
            view.hacim.text = prepareDecimalforUI(stock.volume, Constraints.DECIMAL_ZERO)
            view.alis.text = prepareDecimalforUI(stock.bid, Constraints.DECIMAL_SIZE)
            view.satis.text = prepareDecimalforUI(stock.offer, Constraints.DECIMAL_SIZE)
            when {
                stock.isDown == true -> {
                    view.degisim.setImageResource(R.drawable.ic_baseline_down)
                }
                stock.isUp == true -> {
                    view.degisim.setImageResource(R.drawable.ic_baseline_up)
                }
                else -> {
                    view.degisim.setImageResource(R.drawable.ic_baseline_minus)
                }
            }
        }

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val context = itemView.context
            val intentDetail = Intent(context, DetailActivity::class.java)
            intentDetail.putExtra(Constraints.INTENT_EXTRA_STOCK_ID, stock!!.id)
            context.startActivity(intentDetail)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateStocks(stocks: ArrayList<StockModel>) {
        stockListCopy = stocks
        stockList.addAll(stocks)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filter(key: String) {
        var text = key
        stockList.clear()
        if (text.isEmpty()) {
            stockList.addAll(stockListCopy)
        } else {
            text = text.lowercase()
            for (item in stockListCopy) {
                if (item.symbol?.lowercase()?.contains(text) == true
                ) {
                    stockList.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }
}