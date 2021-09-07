package com.wms.android.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wms.android.MainActivity
import com.wms.android.R
import com.wms.android.logic.model.Menu
import com.wms.android.ui.adjust.AdjustActivity
import com.wms.android.ui.check.CheckListActivity
import com.wms.android.ui.instock.InstockActivity
import com.wms.android.ui.move.MoveActivity
import com.wms.android.ui.outstock.OutListActivity
import com.wms.android.ui.query.AreanoActivity
import com.wms.android.ui.query.MaterialnoActivity
import com.wms.android.ui.tray.DownTrayActivity
import com.wms.android.ui.tray.UpTrayActivity
import java.util.ArrayList

class MenuAdapter(val context: Context,val menunames:List<Menu>):RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val img_menu : ImageView = view.findViewById(R.id.img_menu)
        val lbl_menu : TextView = view.findViewById(R.id.lbl_menu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_menu,parent,false)

        //点击事件注册
        val viewHolder = ViewHolder(view)
        viewHolder.img_menu.setOnClickListener{
            val position = viewHolder.adapterPosition
            val item = menunames[position]

            when(item.name){
                "UpTray"->context.startActivity(Intent(context,UpTrayActivity::class.java))
                "DownTray"->context.startActivity(Intent(context, DownTrayActivity::class.java))
                "ReceiveScan"->context.startActivity(Intent(context,InstockActivity::class.java))
                "SendList"->context.startActivity(Intent(context,OutListActivity::class.java))
                "MoveScan"->context.startActivity(Intent(context,MoveActivity::class.java))
                "StockAdjust"->context.startActivity(Intent(context, AdjustActivity::class.java))
                "CheckInventoryList"->context.startActivity(Intent(context,CheckListActivity::class.java))
                "MaterialQuery"->context.startActivity(Intent(context,MaterialnoActivity::class.java))
                "AreaQuery"->context.startActivity(Intent(context,AreanoActivity::class.java))
            }
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return menunames.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menu = menunames[position]
        holder.lbl_menu.text = menu.zh_name
        Glide.with(context).load(menu.img).into(holder.img_menu)
    }

}