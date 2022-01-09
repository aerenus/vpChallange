package com.erenus.vp.imkb.detail

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.erenus.vp.R
import com.erenus.vp.databinding.DetailActivityBinding
import com.erenus.vp.network.models.DetailModel
import com.erenus.vp.utils.*
import com.erenus.vp.network.models.DetailModelOutgoing
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.data.*
import kotlinx.android.synthetic.main.detail_activity.*

import com.github.mikephil.charting.data.LineData

import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Utils


class DetailActivity : AppCompatActivity(), OnChartValueSelectedListener {

    private lateinit var viewModel: DetailViewModel
    private val prefRepository by lazy { PrefRepository(this) }
    private val cryptoUtil by lazy { CryptoUtil(this) }
    private lateinit var detailActivityBinding: DetailActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        detailActivityBinding = DetailActivityBinding.inflate(layoutInflater)
        detailActivityBinding.lifecycleOwner = this
        detailActivityBinding.vm = viewModel
        setContentView(detailActivityBinding.root)

        getStockDetail()
    }

    fun getStockDetail() {
        viewModel.loading(true)
        viewModel.getStockDetail(prefRepository.getString(Constraints.AUTHORIZATION), prepareStockOutgoing())?.observe(this, {
            when {
                it == null -> {
                    viewModel.loading(false)
                    notificationUtil(R.string.err_UzakSunucuBaglantisiYok, this)
                }
                it.status?.isSuccess == false -> {
                    viewModel.loading(false)
                    this.longToast(it.status.error?.message)
                    finish()
                }
                else -> {
                    prepareStockDetail(it, false)
                    viewModel.loading(false)
                }
            }
        })
    }

    fun prepareStockDetail(detailModel: DetailModel, refresh: Boolean?) {
        val b = detailActivityBinding
        title = resources.getString(R.string.IMKBHisseVeEndeskler) + " › " +  detailModel.symbol?.let { cryptoUtil.decrypt(it) }

        b.tvSembol.text = detailModel.symbol?.let { cryptoUtil.decrypt(it) }
        b.tvFiyat.text = prepareDecimalforUI(detailModel.price, Constraints.DECIMAL_SIZE)
        b.tvFark.text = prepareDecimalforUI(detailModel.difference, Constraints.DECIMAL_ONE)
        b.tvHacim.text = prepareDecimalforUI(detailModel.volume, Constraints.DECIMAL_SIZE)
        b.tvAlis.text = prepareDecimalforUI(detailModel.bid, Constraints.DECIMAL_SIZE)
        b.tvSatis.text = prepareDecimalforUI(detailModel.offer, Constraints.DECIMAL_SIZE)

        b.tvTaban.text = prepareDecimalforUI(detailModel.lowest, Constraints.DECIMAL_SIZE)
        b.tvTavan.text = prepareDecimalforUI(detailModel.highest, Constraints.DECIMAL_SIZE)
        b.tvAdet.text = prepareDecimalforUI(detailModel.count, Constraints.DECIMAL_SIZE)
        b.tvGunlukYuksek.text = prepareDecimalforUI(detailModel.maximum, Constraints.DECIMAL_SIZE)
        b.tvGunlukDusuk.text = prepareDecimalforUI(detailModel.minimum, Constraints.DECIMAL_SIZE)

        when {
            detailModel.isDown == true -> {
                b.degisim.setImageResource(R.drawable.ic_baseline_down)
            }
            detailModel.isUp == true -> {
                b.degisim.setImageResource(R.drawable.ic_baseline_up)
            }
            else -> {
                b.degisim.setImageResource(R.drawable.ic_baseline_minus)
            }
        }

        if(detailModel.graphicData != null && detailModel.graphicData.size > 0) {
            val lineEntries = ArrayList<Entry>()
            var max = 0.0
            var min = 0.0
             for(i in 0 until detailModel.graphicData.size) {
                if(detailModel.graphicData[i].value != null && detailModel.graphicData[i].day != null) {
                    lineEntries.add(Entry(i.toFloat(), detailModel.graphicData[i].value!!.toFloat()))
                    if(max < detailModel.graphicData[i].value!!) max = detailModel.graphicData[i].value!!
                    if(min > detailModel.graphicData[i].value!!) min = detailModel.graphicData[i].value!!
                }
            }


            b.lineChart.description.isEnabled = false
            b.lineChart.setTouchEnabled(true)
            b.lineChart.isDragEnabled = true
            b.lineChart.setScaleEnabled(true)
            b.lineChart.setPinchZoom(true)
            //dokümanda x axis değerleri yok ama çizgisi var.
            //enableGridDashedLine da ekran görüntüsündeki üst değerler olmadan çizimi yapmıyor.
            //ya component güncelleme almış, değişmiş ya da dokümandaki SS de üst kısım crop edilmiş.
            b.lineChart.xAxis.isEnabled = false
            b.lineChart.axisRight.isEnabled = false
            b.lineChart.legend.isEnabled = false

            /* Marker */
            b.lineChart.setOnChartValueSelectedListener(this)
            val mv = MyMakerView(this, R.layout.custom_maker_view)
            mv.chartView = b.lineChart
            b.lineChart.marker = mv

            /* Upper Limit */
            val ll2 = LimitLine(max.toFloat(), "Upper Limit")
            ll2.lineWidth = 4f
            ll2.enableDashedLine(10f, 10f, 0f)
            ll2.labelPosition = LimitLabelPosition.RIGHT_TOP
            ll2.textSize = 10f


            /* Axis */
            val yAxis = b.lineChart.axisLeft
            val xAxis = b.lineChart.xAxis
            xAxis.enableGridDashedLine(30f, 30f, 0f)
            yAxis.enableGridDashedLine(30f, 30f, 0f)
            yAxis.axisMaximum = max.toFloat()*1.8f
            yAxis.axisMinimum = min.toFloat()
            yAxis.setDrawLimitLinesBehindData(true)
            yAxis.addLimitLine(ll2)


            /* Dataset */
            val lineDataSet = LineDataSet(lineEntries, "DataSet 1")
            lineDataSet.setDrawIcons(false)
            lineDataSet.enableDashedLine(30f, 15f, 0f)
            lineDataSet.color = Color.BLACK
            lineDataSet.setCircleColor(Color.BLACK)
            lineDataSet.lineWidth = 1f
            lineDataSet.circleRadius = 3f
            lineDataSet.setDrawCircleHole(false)
            lineDataSet.enableDashedHighlightLine(20f, 10f, 0f)
            lineDataSet.setDrawFilled(true)
            lineDataSet.setDrawValues(false)
            lineDataSet.fillFormatter = IFillFormatter { _, _ ->
                b.lineChart.axisLeft.axisMinimum
            }

            if (Utils.getSDKInt() >= 18) {
                val drawable = ContextCompat.getDrawable(this, R.drawable.fade_red)
                lineDataSet.fillDrawable = drawable
            } else {
                lineDataSet.fillColor = Color.BLACK
            }

            /* Finish */
            val lineData = LineData(lineDataSet)
            b.lineChart.data = lineData
            b.lineChart.invalidate()
            b.lineChart.refreshDrawableState()
            b.lineChart.setNoDataText(resources.getString(R.string.birHataOlustu))
        }
    }

    private fun prepareStockOutgoing(): DetailModelOutgoing {
        val intent = intent
        val key = intent.getIntExtra(Constraints.INTENT_EXTRA_STOCK_ID, 0)
        return DetailModelOutgoing(key.let { cryptoUtil.encrypt(it.toString()) })
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }

    override fun onNothingSelected() {
    }


}