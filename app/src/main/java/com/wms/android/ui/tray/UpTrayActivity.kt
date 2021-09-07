package com.wms.android.ui.tray

import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.wms.android.R
import com.wms.android.adapter.TrayAdapter
import com.wms.android.base.BaseActivity
import com.wms.android.logic.model.OutboxBarcode
import com.wms.android.logic.model.ResultOne
import com.wms.android.logic.network.ServiceCreator
import com.wms.android.logic.network.TrayService
import com.wms.android.util.focus
import com.wms.android.util.showToast
import kotlinx.android.synthetic.main.activity_up_tray.*


class UpTrayActivity : BaseActivity() {

    //定义服务
    private val service = ServiceCreator.create(TrayService::class.java)

    //定义list
    private lateinit var list: ArrayList<OutboxBarcode>
    private lateinit var adapter : TrayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_up_tray)
        init()

        txt_serialno.setOnKeyListener{ _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (txt_serialno.text.isNullOrEmpty()) {
                    showToast("请扫描条码！")
                    return@setOnKeyListener true
                }
                service.trayScanFirst(txt_serialno.text.toString())
                    .enqueue(ServiceCreator.go(
                        fun(res: ResultOne<OutboxBarcode>) {
                            lbl_total.text = res.total.toString()
                            lbl_trayno.text = res.list[0].trayno
                            list = res.list
                            createView()
                            txt_serialno2.focus()
                        }, fun() {
                            txt_serialno.focus()
                        })
                    )
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        txt_serialno2.setOnKeyListener{ _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (txt_serialno2.text.isNullOrEmpty()) {
                    showToast("请扫描条码！")
                    return@setOnKeyListener true
                }
                if(list.isEmpty())return@setOnKeyListener true

                val barcode = OutboxBarcode().apply {
                    serialno=txt_serialno2.text.toString()
                    trayno=list[0].trayno
                    materialno=list[0].materialno
                }
                service.trayScanOther(barcode)
                    .enqueue(ServiceCreator.go(
                        fun(res: ResultOne<OutboxBarcode>) {
                            val model = res.model
                            list.add(model);
                            adapter.notifyDataSetChanged()
                            lbl_total.text = (lbl_total.text.toString().toInt()+1).toString();
                            txt_serialno2.focus()
                        }, fun() {
                            txt_serialno2.focus()
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
        adapter = TrayAdapter(list)
        rv.adapter = adapter
    }
}