package com.wms.android.ui.move

import android.os.Bundle
import android.view.KeyEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.wms.android.R
import com.wms.android.adapter.MoveAdapter
import com.wms.android.base.BaseActivity
import com.wms.android.logic.model.*
import com.wms.android.logic.network.MoveService
import com.wms.android.logic.network.ServiceCreator
import com.wms.android.util.focus
import com.wms.android.util.showToast
import kotlinx.android.synthetic.main.activity_outstock.rv
import kotlinx.android.synthetic.main.activity_move.rd_tray
import kotlinx.android.synthetic.main.activity_move.txt_areano
import kotlinx.android.synthetic.main.activity_outstock.rd_box
import kotlinx.android.synthetic.main.activity_outstock.txt_serialno


class MoveActivity : BaseActivity() {

    private var sendtype: String = "box"


    //定义服务
    private val service = ServiceCreator.create(MoveService::class.java)

    //定义list
    private var list: ArrayList<Stock> = ArrayList<Stock>()
    private var adapter: MoveAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_move)

        init()
        txt_areano.setOnKeyListener{ _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (txt_areano.text.isNullOrEmpty()) {
                    showToast("请扫描库位！")
                    return@setOnKeyListener true
                }
                service.scanArea(txt_areano.text.toString())
                    .enqueue(ServiceCreator.go(
                        fun(res: ResultOne<Area>) {
                            txt_serialno.focus()
                        }, fun() {
                            txt_areano.focus()
                        })
                    )
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        txt_serialno.setOnKeyListener{ _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (txt_serialno.text.isNullOrEmpty()) {
                    showToast("请扫描条码！")
                    return@setOnKeyListener true
                }
                if (txt_areano.text.isNullOrEmpty()) {
                    showToast("请扫描库位！")
                    txt_areano.focus()
                    return@setOnKeyListener true
                }
                service.scanBarcode(txt_areano.text.toString(),txt_serialno.text.toString(),sendtype, User.user.name)
                    .enqueue(ServiceCreator.go(
                        fun(res: ResultOne<Stock>) {
                            list = res.list
                            createView()
                            txt_serialno.focus()
                        }, fun() {
                            txt_serialno.focus()
                        })
                    )
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        rd_tray.setOnClickListener { setRd("tray") }
        rd_box.setOnClickListener { setRd("box") }
    }
    private fun setRd(type: String) {
        txt_serialno.setText("")
        txt_areano.setText("")
        txt_areano.focus()
        when (type) {
            "tray" -> {
                rd_tray.isChecked = true
                rd_box.isChecked = false
                sendtype = "tray"
            }
            "box" -> {
                rd_tray.isChecked = false
                rd_box.isChecked = true
                sendtype = "box"
            }
        }
    }

    private fun init() {
        txt_areano.focus()
    }
    private fun createView() {
        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager
        adapter = MoveAdapter(list)
        rv.adapter = adapter
    }
}