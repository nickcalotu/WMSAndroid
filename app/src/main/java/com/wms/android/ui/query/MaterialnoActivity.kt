package com.wms.android.ui.query

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.wms.android.R
import com.wms.android.adapter.CheckQueryAdapter
import com.wms.android.adapter.OutstockAdapter
import com.wms.android.adapter.QueryAdapter
import com.wms.android.base.BaseActivity
import com.wms.android.logic.model.CheckArea
import com.wms.android.logic.model.CheckDetail
import com.wms.android.logic.model.ResultOne
import com.wms.android.logic.model.Stock
import com.wms.android.logic.network.QueryService
import com.wms.android.logic.network.ServiceCreator
import com.wms.android.util.focus
import com.wms.android.util.showToast
import kotlinx.android.synthetic.main.activity_materialno.*


class MaterialnoActivity : BaseActivity() {

    private val service = ServiceCreator.create(QueryService::class.java)
    //定义list
    private var list: ArrayList<Stock> = ArrayList<Stock>()
    private var adapter: QueryAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materialno)
        init()

        txt_serialno.setOnKeyListener{ _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (txt_serialno.text.isNullOrEmpty()) {
                    showToast("请扫描条码！")
                    return@setOnKeyListener true
                }
                service.hhtStockQueryByMaterial(txt_serialno.text.toString())
                    .enqueue(ServiceCreator.go(
                        fun(res: ResultOne<Stock>) {
                            list = res.list
                            createView()
                            if(list.isEmpty()) showToast("没有数据！！")
                            txt_serialno.focus()
                        }, fun() {
                            txt_serialno.focus()
                        })
                    )
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun init() {
        txt_serialno.focus()
    }
    private fun createView() {
        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager
        adapter = QueryAdapter(list)
        rv.adapter = adapter
    }
}