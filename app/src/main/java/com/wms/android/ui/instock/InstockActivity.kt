package com.wms.android.ui.instock

import android.opengl.Visibility
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.wms.android.R
import com.wms.android.adapter.InstockAdapter
import com.wms.android.base.BaseActivity
import com.wms.android.logic.model.*
import com.wms.android.logic.network.InstockService
import com.wms.android.logic.network.ServiceCreator
import com.wms.android.util.focus
import com.wms.android.util.showAlertDialog
import com.wms.android.util.showToast
import kotlinx.android.synthetic.main.activity_down_tray.*
import kotlinx.android.synthetic.main.activity_instock.*
import kotlinx.android.synthetic.main.activity_instock.rv
import kotlinx.android.synthetic.main.activity_instock.txt_serialno
import kotlinx.android.synthetic.main.board.view.*



class InstockActivity : BaseActivity() {

    //定义服务
    private val service = ServiceCreator.create(InstockService::class.java)

    //定义list
    private var head: Voucher? = null
    private var list: ArrayList<VoucherDetail> = ArrayList<VoucherDetail>()
    private var adapter : InstockAdapter? = null
    private var tempvoucherno:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instock)
        init()

        txt_voucherno.setOnKeyListener{ _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (txt_voucherno.text.isNullOrEmpty()) {
                    showToast("请扫描单据！")
                    return@setOnKeyListener true
                }
                service.getVoucher(txt_voucherno.text.toString())
                    .enqueue(ServiceCreator.go(
                        fun(res: ResultTwo<Voucher,VoucherDetail>) {
                            head = res.model
                            list = res.list
                            createView()
                            tempvoucherno = txt_voucherno.text.toString()
                            txt_voucherno.setText(head?.voucherno)
                            txt_areano.focus()
                        }, fun() {
                            txt_voucherno.focus()
                        })
                    )
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

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
                if(list==null || list!!.isEmpty()){
                    showToast("请扫描单据！")
                    txt_voucherno.focus()
                    return@setOnKeyListener true
                }
                val data = Tasktrans().apply {
                    serialno=txt_serialno.text.toString()
                    toareano = txt_areano.text.toString()
                    voucherno = head?.voucherno
                    vouchertype = head?.vouchertype
                }
                service.scanBarcode(data,User.user.name)
                    .enqueue(ServiceCreator.go(
                        fun(res: ResultOne<OutboxBarcode>) {
                            var sum:Double =0.0
                            res.list.forEach {
                                sum += it.qty!!
                            }
                            val model = res.list[0];
                            model.qty = sum;
                            makeBoard(model)
                            service.getVoucher(tempvoucherno)
                                .enqueue(ServiceCreator.go(
                                    fun(res: ResultTwo<Voucher,VoucherDetail>) {
                                        head = res.model;
                                        list = res.list
                                        createView()
                                        txt_serialno.focus()
                                    }, fun() {
                                        txt_serialno.focus()
                                    })
                                )
                        }, fun() {
                            txt_serialno.focus()
                        })
                    )
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        btn_submit.setOnClickListener {
            if(list.isEmpty()){
                showToast("请扫描单据！")
                txt_voucherno.focus()
                return@setOnClickListener
            }
            showAlertDialog(this,"提示","确认过账吗？"){
                service.btnPost(head!!.id)
                    .enqueue(ServiceCreator.go(
                        fun(res: ResultOne<String>) {
                            showToast("过账成功")
                            finish()
                        }, fun() {

                        })
                    )
            }

        }
    }

    private fun init() {
        txt_voucherno.focus()
        board.lbl_areano.visibility = View.GONE
    }

    private fun makeBoard(model:OutboxBarcode){
        with(board){
            lbl_materialno.text ="编号:"+ model.materialno
            lbl_batchno.text ="批次:"+ model.batchno
            lbl_materialname.text = "描述:"+model.materialname
            lbl_qty.text ="数量:"+ model.qty.toString()
        }
    }

    private fun createView() {
        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager
        adapter = InstockAdapter(list)
        rv.adapter = adapter
    }
}