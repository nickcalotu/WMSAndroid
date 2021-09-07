package com.wms.android.ui.outstock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wms.android.R
import com.wms.android.adapter.OutstockAdapter
import com.wms.android.adapter.OutstockQueryAdapter
import com.wms.android.base.BaseActivity
import com.wms.android.logic.model.*
import com.wms.android.logic.network.OutstockService
import com.wms.android.logic.network.ServiceCreator
import com.wms.android.util.showAlertDialog
import com.wms.android.util.showToast
import kotlinx.android.synthetic.main.activity_outstock_query.*

class OutstockQueryActivity : BaseActivity() {

    private var item:TaskDetail? = null
    //定义服务
    private val service = ServiceCreator.create(OutstockService::class.java)
    //定义list
    private var list: ArrayList<Tasktrans> = ArrayList<Tasktrans>()
    private var adapter: OutstockQueryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outstock_query)

        item = intent.getSerializableExtra("item") as TaskDetail
        val taskno = intent.getStringExtra("taskno")
        service.PDAGetTasktransByTaskno(taskno!!, item?.materialno!!)
            .enqueue(ServiceCreator.go(
                fun(res: ResultOne<Tasktrans>) {
                    list = res.list
                    createView()
                }, fun() {

                })
            )
    }

    private fun createView() {
        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager
        adapter = OutstockQueryAdapter(this,list){item->
            showAlertDialog(this, "提示", "此操作将删除对应的扫描信息, 是否继续?") {
                service.RollbackTaskTrans(item.id,User.user.name!!)
                    .enqueue(ServiceCreator.go(
                        fun(res: ResultOne<String>) {
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