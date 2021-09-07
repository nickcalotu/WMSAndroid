package com.wms.android.ui.check

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wms.android.R
import com.wms.android.adapter.CheckQueryAdapter
import com.wms.android.adapter.OutstockAdapter
import com.wms.android.adapter.OutstockQueryAdapter
import com.wms.android.base.BaseActivity
import com.wms.android.logic.model.*
import com.wms.android.logic.network.CheckService
import com.wms.android.logic.network.OutstockService
import com.wms.android.logic.network.ServiceCreator
import com.wms.android.util.showAlertDialog
import com.wms.android.util.showToast
import kotlinx.android.synthetic.main.activity_outstock_query.*

class CheckQueryActivity : BaseActivity() {

    private var checkno:String? = null
    //定义服务
    private val service = ServiceCreator.create(CheckService::class.java)
    //定义list
    private var list: ArrayList<CheckDetail> = ArrayList<CheckDetail>()
    private var adapter: CheckQueryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outstock_query)

        checkno = intent.getStringExtra("checkno")
        service.getCheckDetailsGroup(checkno!!)
            .enqueue(ServiceCreator.go(
                fun(res: ResultOne<CheckDetail>) {
                    list = res.list
                    if(list.isEmpty()){
                        showToast("没有扫描记录")
                    }
                    createView()
                }, fun() {

                })
            )
    }

    private fun createView() {
        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager
        adapter = CheckQueryAdapter(this,list){ item->
            showAlertDialog(this, "提示", "此操作将删除对应的扫描信息, 是否继续?") {
                service.delCheckDetail(checkno!!,item.areano!!,item.materialno!!)
                    .enqueue(ServiceCreator.go(
                        fun(res: ResultOne<CheckDetail>) {
                            showToast("删除成功")
                            list.remove(item)
                            adapter?.notifyDataSetChanged()
                            if(list.isEmpty()) finish()
                        }, fun() {

                        })
                    )
            }
        }
        rv.adapter = adapter
    }
}