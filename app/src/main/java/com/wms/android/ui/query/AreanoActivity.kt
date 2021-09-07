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
import com.wms.android.logic.network.CheckService
import com.wms.android.logic.network.QueryService
import com.wms.android.logic.network.ServiceCreator
import com.wms.android.util.focus
import com.wms.android.util.showToast
import kotlinx.android.synthetic.main.activity_areano.*
import kotlinx.android.synthetic.main.activity_areano.txt_areano


class AreanoActivity : BaseActivity() {

    private val service = ServiceCreator.create(QueryService::class.java)
    //定义list
    private var list: ArrayList<Stock> = ArrayList<Stock>()
    private var adapter: QueryAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_areano)
        init()

        txt_areano.setOnKeyListener{ _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (txt_areano.text.isNullOrEmpty()) {
                    showToast("请扫描库位！")
                    return@setOnKeyListener true
                }
                service.hhtStockQueryByArea(txt_areano.text.toString())
                    .enqueue(ServiceCreator.go(
                        fun(res: ResultOne<Stock>) {
                            list = res.list
                            createView()
                            if(list.isEmpty()) showToast("没有数据！！")
                            txt_areano.focus()
                        }, fun() {
                            txt_areano.focus()
                        })
                    )
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun init() {
        txt_areano.focus()
    }
    private fun createView() {
        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager
        adapter = QueryAdapter(list)
        rv.adapter = adapter
    }
}