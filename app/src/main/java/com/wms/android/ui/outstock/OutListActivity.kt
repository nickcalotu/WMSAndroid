package com.wms.android.ui.outstock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.wms.android.R
import com.wms.android.adapter.InstockAdapter
import com.wms.android.adapter.OutListAdapter
import com.wms.android.base.BaseActivity
import com.wms.android.logic.model.*
import com.wms.android.logic.network.InstockService
import com.wms.android.logic.network.OutstockService
import com.wms.android.logic.network.ServiceCreator
import com.wms.android.util.focus
import com.wms.android.util.hideKeyBoard
import com.wms.android.util.showToast
import kotlinx.android.synthetic.main.activity_check_list.*
import kotlinx.android.synthetic.main.activity_instock.*
import kotlinx.android.synthetic.main.activity_instock.rv
import kotlinx.android.synthetic.main.activity_out_list.*
import kotlinx.android.synthetic.main.activity_out_list.txt_search

class OutListActivity : BaseActivity() {

    //定义服务
    private val service = ServiceCreator.create(OutstockService::class.java)

    //定义list
    private var list: ArrayList<Task> = ArrayList<Task>()
    private var adapter: OutListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_out_list)

        txt_search.isIconifiedByDefault = false

        txt_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {

                if (p0 != null) {
                    service.getTasks(p0)
                        .enqueue(
                            ServiceCreator.go(
                                fun(res: ResultOne<Task>) {
                                    list = res.list
                                    createView()
                                })
                        )
                }

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
    }

    override fun onStart() {
        super.onStart()
        service.getTasks("")
            .enqueue(
                ServiceCreator.go(
                    fun(res: ResultOne<Task>) {
                        list = res.list
                        createView()
                    })
            )
    }
    private fun createView() {
        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager
        adapter = OutListAdapter(list)
        rv.adapter = adapter
    }
}