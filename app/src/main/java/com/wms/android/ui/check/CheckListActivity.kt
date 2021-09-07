package com.wms.android.ui.check

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.wms.android.R
import com.wms.android.adapter.CheckListAdapter
import com.wms.android.adapter.OutListAdapter
import com.wms.android.base.BaseActivity
import com.wms.android.logic.model.*
import com.wms.android.logic.network.CheckService
import com.wms.android.logic.network.ServiceCreator
import com.wms.android.util.hideKeyBoard
import kotlinx.android.synthetic.main.activity_check_list.*
import kotlinx.android.synthetic.main.activity_instock.rv


class CheckListActivity : BaseActivity() {

    //定义服务
    private val service = ServiceCreator.create(CheckService::class.java)

    //定义list
    private var list: ArrayList<Check> = ArrayList<Check>()
    private var adapter: CheckListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_list)

        txt_search.isIconifiedByDefault = false
        //txt_search.onActionViewExpanded()

        txt_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {

                if (p0 != null) {
                    service.getChecks(p0)
                        .enqueue(
                            ServiceCreator.go(
                                fun(res: ResultOne<Check>) {
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
        service.getChecks("")
            .enqueue(
                ServiceCreator.go(
                    fun(res: ResultOne<Check>) {
                        list = res.list
                        createView()
                    })
            )
    }
    private fun createView() {
        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager
        adapter = CheckListAdapter(list)
        rv.adapter = adapter
    }
}