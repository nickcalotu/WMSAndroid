package com.wms.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wms.android.R
import com.wms.android.logic.model.Menu
import com.wms.android.logic.model.OutboxBarcode

class TrayAdapter(val list: List<OutboxBarcode>) : RecyclerView.Adapter<TrayAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lbl_materialno: TextView = view.findViewById(R.id.lbl_materialno)
        val lbl_materialname: TextView = view.findViewById(R.id.lbl_materialname)
        val lbl_qty: TextView = view.findViewById(R.id.lbl_qty)
        val lbl_batchno: TextView = view.findViewById(R.id.lbl_batchno)
        val lbl_serialno: TextView = view.findViewById(R.id.lbl_serialno)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tray, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.lbl_materialno.text = "编号:" + item.materialno
        holder.lbl_materialname.text ="描述:" + item.materialname
        holder.lbl_qty.text ="数量:" + item.qty.toString()
        holder.lbl_batchno.text ="批次:" + item.batchno
        holder.lbl_serialno.text ="序列号:" + item.serialno


    }

}