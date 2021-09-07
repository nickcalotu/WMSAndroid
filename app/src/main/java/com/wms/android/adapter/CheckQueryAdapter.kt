package com.wms.android.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wms.android.R
import com.wms.android.base.BaseApplication
import com.wms.android.logic.model.*
import com.wms.android.logic.network.ServiceCreator
import com.wms.android.ui.outstock.OutstockActivity
import com.wms.android.ui.outstock.OutstockQueryActivity
import com.wms.android.util.showAlertDialog
import com.wms.android.util.showToast
import java.io.File


class CheckQueryAdapter(val context: Context, val list: List<CheckDetail>, val block:((item:CheckDetail)->Unit)) : RecyclerView.Adapter<CheckQueryAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lbl_materialno: TextView = view.findViewById(R.id.lbl_materialno)
        val lbl_materialname: TextView = view.findViewById(R.id.lbl_materialname)
        val lbl_qty: TextView = view.findViewById(R.id.lbl_qty)
        val lbl_areano: TextView = view.findViewById(R.id.lbl_areano)
        val linear: LinearLayout = view.findViewById(R.id.linear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_check_query, parent, false)
        //点击事件注册
        val viewHolder = ViewHolder(view)
        viewHolder.linear.setOnClickListener{
            val position = viewHolder.adapterPosition
            val item = list[position]
            block(item)
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.lbl_materialno.text = "编号:" + item.materialno
        holder.lbl_materialname.text ="描述:" + item.materialname
        holder.lbl_qty.text ="数量:" + item.qty.toString()
        holder.lbl_qty.setTextColor(Color.rgb(245,108,108))
        holder.lbl_areano.text ="库位:" + item.areano
    }


}