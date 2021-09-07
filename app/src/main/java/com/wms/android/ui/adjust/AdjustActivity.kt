package com.wms.android.ui.adjust

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.wms.android.R
import com.wms.android.base.BaseActivity
import com.wms.android.logic.model.*
import com.wms.android.logic.network.AdjustService
import com.wms.android.logic.network.ServiceCreator
import com.wms.android.util.focus
import com.wms.android.util.hideKeyBoard
import com.wms.android.util.showToast
import kotlinx.android.synthetic.main.activity_adjust.*
import kotlinx.android.synthetic.main.activity_adjust.board
import kotlinx.android.synthetic.main.activity_adjust.btn_submit
import kotlinx.android.synthetic.main.activity_adjust.txt_serialno
import kotlinx.android.synthetic.main.board.view.*



class AdjustActivity : BaseActivity() {

    //定义服务
    private val service = ServiceCreator.create(AdjustService::class.java)

    private var model: Stock? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adjust)
        init()

        txt_serialno.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (txt_serialno.text.isNullOrEmpty()) {
                    showToast("请扫描条码！")
                    return@setOnKeyListener true
                }
                service.scanBarcode(txt_serialno.text.toString())
                    .enqueue(ServiceCreator.go(
                        fun(res: ResultOne<Stock>) {
                            model = res.model;
                            makeBoard(model!!)
                            val message = res.model.message
                            txt_areano.setText(res.model.areano)
                            txt_qty.setText(res.model.qty.toString())
                            if (message == "库存") {
                                txt_areano.visibility = View.INVISIBLE
                                txt_qty.focus()
                            } else {
                                txt_areano.visibility = View.VISIBLE
                                txt_areano.focus()
                            }
                        }, fun() {
                            txt_serialno.focus()
                        })
                    )
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        txt_qty.setOnKeyListener{_, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                hideKeyBoard(window)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        btn_submit.setOnClickListener {
            if (txt_serialno.text.isNullOrEmpty()) {
                showToast("请扫描条码！")
                return@setOnClickListener
            }
            if (txt_areano.text.isNullOrEmpty()) {
                showToast("请扫描库位！")
                txt_areano.focus()
                return@setOnClickListener
            }
            if (txt_qty.text.isNullOrEmpty() || txt_qty.text.toString() == "0") {
                showToast("数量不能为0！")
                txt_qty.focus()
                return@setOnClickListener
            }
            var list: ArrayList<Stock> = ArrayList<Stock>()
            list.add(model!!)
            var temp = model!!.clone()
            temp.qty = txt_qty.text.toString().toDouble()
            temp.areano = txt_areano.text.toString()
            list.add(temp)
            if (temp.qty == model!!.qty && temp.areano == model!!.areano) {
                showToast("没有修改数据，不能提交！")
                return@setOnClickListener
            }
            btn_submit.isEnabled = false

            service.btnModify(list,User.user.name)
                .enqueue(ServiceCreator.go(
                    fun(res: ResultOne<String>) {
                        showToast(res.message)
                        txt_serialno.focus()
                        model = null
                        clearBoard()
                        txt_areano.setText("")
                        txt_qty.setText("")
                        btn_submit.isEnabled = true
                    }, fun() {
                        btn_submit.isEnabled = true
                    })
                )
        }

        btn_out.setOnClickListener {
            if (model==null||model?.materialno.isNullOrEmpty()) {
                showToast("请扫描条码！")
                return@setOnClickListener
            }
            btn_out.isEnabled = false

            service.btnSend(model!!,User.user.name)
                .enqueue(ServiceCreator.go(
                    fun(res: ResultOne<String>) {
                        showToast(res.message)
                        txt_serialno.focus()
                        model = null
                        clearBoard()
                        txt_areano.setText("")
                        txt_qty.setText("")
                        btn_out.isEnabled = true
                    }, fun() {
                        btn_out.isEnabled = true
                    })
                )
        }
    }

    private fun init() {
        txt_serialno.focus()
    }

    private fun makeBoard(model: Stock) {
        with(board) {
            lbl_materialno.text = "编号:" + model.materialno
            lbl_batchno.text = "批次:" + model.batchno
            lbl_materialname.text = "描述:" + model.materialname
            lbl_qty.text = "数量:" + model.qty.toString()
            lbl_areano.text = "库位:"+model.areano
        }
    }
    private fun clearBoard() {
        with(board) {
            lbl_materialno.text = "编号:"
            lbl_batchno.text = "批次:"
            lbl_materialname.text = "描述:"
            lbl_qty.text = "数量:"
            lbl_areano.text = "库位:"
        }
    }

}