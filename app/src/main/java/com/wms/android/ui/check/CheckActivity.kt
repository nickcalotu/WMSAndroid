package com.wms.android.ui.check

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.wms.android.MainActivity
import com.wms.android.R
import com.wms.android.adapter.OutstockAdapter
import com.wms.android.base.BaseActivity
import com.wms.android.base.BaseApplication
import com.wms.android.logic.model.*
import com.wms.android.logic.network.CheckService
import com.wms.android.logic.network.OutstockService
import com.wms.android.logic.network.ServiceCreator
import com.wms.android.ui.outstock.OutstockQueryActivity
import com.wms.android.util.focus
import com.wms.android.util.showToast
import kotlinx.android.synthetic.main.activity_adjust.*
import kotlinx.android.synthetic.main.activity_check.*
import kotlinx.android.synthetic.main.activity_check.board
import kotlinx.android.synthetic.main.activity_check.btn_submit
import kotlinx.android.synthetic.main.activity_check.txt_areano
import kotlinx.android.synthetic.main.activity_check.txt_qty
import kotlinx.android.synthetic.main.activity_check.txt_serialno
import kotlinx.android.synthetic.main.board.view.*
import java.util.ArrayList

class CheckActivity : BaseActivity() {


    private var checkno: String? = ""

    //定义服务
    private val service = ServiceCreator.create(CheckService::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)

        init()

        txt_areano.setOnKeyListener{ _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (txt_areano.text.isNullOrEmpty()) {
                    showToast("请扫描库位！")
                    return@setOnKeyListener true
                }
                service.scanArea(checkno!!,txt_areano.text.toString())
                    .enqueue(ServiceCreator.go(
                        fun(res: ResultOne<CheckArea>) {
                            txt_serialno.focus()
                        }, fun() {
                            txt_areano.focus()
                        })
                    )
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        txt_serialno.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (txt_serialno.text.isNullOrEmpty()) {
                    showToast("请扫描条码！")
                    return@setOnKeyListener true
                }
                service.scanBarcode(txt_serialno.text.toString())
                    .enqueue(ServiceCreator.go(
                        fun(res: ResultOne<Stock>) {
                            var model = res.model;
                            makeBoard(model!!)
                            txt_qty.setText(res.model.qty.toString())
                            txt_qty.focus()
                        }, fun() {
                            txt_serialno.focus()
                        })
                    )
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
            btn_submit.isEnabled = false

            service.btnSubmit(checkno!!,txt_areano.text.toString(),txt_serialno.text.toString(),txt_qty.text.toString().toDouble(),User.user.name!!)
                .enqueue(ServiceCreator.go(
                    fun(res: ResultOne<CheckDetail>) {
                        showToast(res.message)
                        txt_serialno.focus()
                        clearBoard()
                        txt_areano.setText("")
                        txt_qty.setText("")
                        btn_submit.isEnabled = true
                    }, fun() {
                        btn_submit.isEnabled = true
                    })
                )
        }

        btn_search.setOnClickListener {
            val intent = Intent(this, CheckQueryActivity::class.java)
            intent.putExtra("checkno", checkno)
            startActivity(intent)
        }






        btn_areano.setOnClickListener {
            clearBoard()
            txt_serialno.setText("")
            txt_areano.setText("")
            txt_qty.setText("")
            txt_areano.focus()
        }



    }


    private fun init() {
        checkno = intent.getStringExtra("checkno")
        val status = intent.getIntExtra("status",0)
        if(status==1){
            //更新状态
            service.setCheckStatusStart(checkno!!)
                .enqueue(ServiceCreator.go(fun(res: ResultOne<Check>) {}))
        }
        txt_areano.focus()
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