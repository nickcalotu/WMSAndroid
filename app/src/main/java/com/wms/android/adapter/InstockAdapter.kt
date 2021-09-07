package com.wms.android.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wms.android.R
import com.wms.android.logic.model.VoucherDetail


class InstockAdapter(val list: List<VoucherDetail>) : RecyclerView.Adapter<InstockAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lbl_materialno: TextView = view.findViewById(R.id.lbl_materialno)
        val lbl_rowno: TextView = view.findViewById(R.id.lbl_rowno)
        val lbl_materialname: TextView = view.findViewById(R.id.lbl_materialname)
        val lbl_qty: TextView = view.findViewById(R.id.lbl_qty)
        val lbl_recqty: TextView = view.findViewById(R.id.lbl_recqty)
        val bar: ProgressBar = view.findViewById(R.id.bar)
        val lbl_bar: TextView = view.findViewById(R.id.lbl_bar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_instock, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.lbl_materialno.text = "编号:" + item.materialno
        holder.lbl_materialname.text ="描述:" + item.materialname
        holder.lbl_qty.text ="订单总数:" + item.qty.toString()
        holder.lbl_recqty.text ="已入库数:" + item.recqty
        holder.lbl_rowno.text ="行号:" + item.rowno
        val num:Double
        try {
            num = Math.ceil(item.recqty!!/item.qty!!*100)
            holder.lbl_bar.text = num.toInt().toString()+"%"
            when{
                num>100 -> {
                    holder.lbl_recqty.setTextColor(Color.rgb(60,158,255))
//                    val d = ClipDrawable(
//                        ColorDrawable(Color.rgb(60,158,255)),
//                        Gravity.LEFT,
//                        ClipDrawable.HORIZONTAL
//                    )
//                    holder.bar.progressDrawable = d
                }
                num==100.0 -> {
                    holder.lbl_recqty.setTextColor(Color.rgb(19,206,102))
                }
                num<50 ->{
                    holder.lbl_recqty.setTextColor(Color.rgb(245,108,108))
                }
                num>=50 ->{
                    holder.lbl_recqty.setTextColor(Color.rgb(230,162,60))
                }
                else->{

                }
            }
            holder.bar.progress =num.toInt()
        }catch(e:Exception){}
    }

}