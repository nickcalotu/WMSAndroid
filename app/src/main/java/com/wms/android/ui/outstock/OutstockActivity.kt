package com.wms.android.ui.outstock

import android.os.Bundle
import android.view.KeyEvent
import android.view.View


import androidx.recyclerview.widget.LinearLayoutManager
import com.wms.android.R

import com.wms.android.adapter.OutstockAdapter
import com.wms.android.base.BaseActivity
import com.wms.android.logic.model.*

import com.wms.android.logic.network.OutstockService
import com.wms.android.logic.network.ServiceCreator
import com.wms.android.util.*
import kotlinx.android.synthetic.main.activity_outstock.*
import kotlinx.android.synthetic.main.activity_outstock.board
import kotlinx.android.synthetic.main.activity_outstock.btn_submit
import kotlinx.android.synthetic.main.activity_outstock.rv
import kotlinx.android.synthetic.main.activity_outstock.txt_serialno
import kotlinx.android.synthetic.main.board.view.*
import kotlinx.android.synthetic.main.item_outlist.lbl_taskno

class OutstockActivity : BaseActivity() {

    private var taskid: Int = 0
    private var taskno: String = ""
    private var sendtype: String = "box"

    //定义服务
    private val service = ServiceCreator.create(OutstockService::class.java)

    //定义list
    private var list: ArrayList<TaskDetail> = ArrayList<TaskDetail>()
    private var adapter: OutstockAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outstock)

        taskid = intent.getIntExtra("taskid", 0)
        taskno = intent.getStringExtra("taskno")!!
        //lbl_taskno.text = "单号:" + intent.getStringExtra("taskno")
        //getList()
        init()

        txt_qty.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                txt_serialno.focus()
                hideKeyBoard(window)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        txt_serialno.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                scan_serialno()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        btn_submit.setOnClickListener {
            showAlertDialog(this, "提示", "确认过账吗？") {
                service.btnPost(taskid)
                    .enqueue(ServiceCreator.go(
                        fun(res: ResultOne<String>) {
                            showToast("过账成功")
                            finish()
                        }, fun() {

                        })
                    )
            }

        }

        rd_tray.setOnClickListener { setRd("tray") }
        rd_box.setOnClickListener { setRd("box") }
        rd_zero.setOnClickListener { setRd("zero") }
    }

    override fun onStart() {
        super.onStart()
        getList()
    }


    private fun setRd(type: String) {
        txt_serialno.setText("")
        txt_qty.setText("")

        when (type) {
            "tray" -> {
                rd_tray.isChecked = true
                rd_box.isChecked = false
                rd_zero.isChecked = false
                txt_qty.visibility = View.GONE
                lbl_qty.visibility = View.GONE
                txt_serialno.focus()
                sendtype = "tray"
            }
            "box" -> {
                rd_tray.isChecked = false
                rd_box.isChecked = true
                rd_zero.isChecked = false
                txt_qty.visibility = View.GONE
                lbl_qty.visibility = View.GONE
                txt_serialno.focus()
                sendtype = "box"
            }
            "zero" -> {
                rd_tray.isChecked = false
                rd_box.isChecked = false
                rd_zero.isChecked = true
                txt_qty.visibility = View.VISIBLE
                lbl_qty.visibility = View.VISIBLE
                txt_qty.focus()
                sendtype = "zero"
            }
        }
    }

    private fun scan_serialno() {
        val serialno = txt_serialno.text.toString()
        val zeroqty = txt_qty.text.toString().toDouble()
        val username = User.user.name
        if (txt_serialno.text.isNullOrEmpty()) {
            showToast("请扫描条码！")
            txt_serialno.focus()
            return
        }
        if (sendtype == "zero") {
            //零
            if (txt_qty.text.isNullOrEmpty() || txt_qty.text.toString() == "0") {
                showToast("请输入数量！")
                txt_qty.focus()
                return
            }
            service.scanBarcode(taskid, serialno, sendtype, zeroqty, username!!)
                .enqueue(ServiceCreator.go(
                    fun(res: ResultTwo<Stock, TaskDetail>) {
                        list = res.list
                        createView()
                        val model = res.model
                        makeBoard(model)
                        txt_serialno.setText("")
                        txt_qty.setText("")
                        txt_qty.focus()
                    }, fun() {
                        txt_serialno.focus()
                    })
                )
        } else {
            service.scanBarcode(taskid, serialno, sendtype, 0.0, username!!)
                .enqueue(ServiceCreator.go(
                    fun(res: ResultTwo<Stock, TaskDetail>) {
                        list = res.list
                        createView()
                        val model = res.model
                        makeBoard(model)
                        txt_serialno.focus()

                    }, fun() {
                        txt_serialno.focus()
                    })
                )
        }
    }

    private fun getList() {
        service.getTaskDetails(taskid)
            .enqueue(ServiceCreator.go(
                fun(res: ResultOne<TaskDetail>) {
                    list = res.list
                    createView()
                }, fun() {

                })
            )
    }

    private fun init() {
        txt_serialno.focus()
    }
    private fun createView() {
        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager
        adapter = OutstockAdapter(taskno, list)
        rv.adapter = adapter
    }
    private fun makeBoard(model: Stock) {
        with(board) {
            lbl_materialno.text = "编号:" + model.materialno
            lbl_batchno.text = "批次:" + model.batchno
            lbl_materialname.text = "描述:" + model.materialname
            lbl_qty.text = "数量:" + model.qty.toString()
            lbl_areano.text = "库位:" + model.areano
        }
    }


}