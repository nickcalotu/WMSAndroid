package com.wms.android

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.wms.android.adapter.MenuAdapter
import com.wms.android.base.BaseActivity
import com.wms.android.logic.model.Menu
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var  menunames:List<String>
    private var list_menu:List<Menu> = mutableListOf(
        Menu("UpTray",R.drawable.up_tray,"组托"),
        Menu("DownTray",R.drawable.dtray,"拆托"),
        Menu("ReceiveScan",R.drawable.in_stock,"收货"),
        Menu("SendList",R.drawable.out_stock,"发货"),
        Menu("MoveScan",R.drawable.inner_move,"移库"),
        Menu("StockAdjust",R.drawable.adjust,"库存调整"),
        Menu("CheckInventoryList",R.drawable.check,"盘点"),
        Menu("MaterialQuery",R.drawable.query,"物料查询"),
        Menu("AreaQuery",R.drawable.query,"库位查询")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menunames = intent.getStringArrayListExtra("menu")!!
        initMenus()
        val layoutManager = GridLayoutManager(this,3)
        rv_menu.layoutManager = layoutManager
        var adapter = MenuAdapter(this,list_menu)
        rv_menu.adapter = adapter

    }

    private fun initMenus() {
        list_menu = list_menu.filter { menunames.contains(it.name) }
    }
}