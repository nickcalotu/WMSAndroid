package com.wms.android.adapter

import android.annotation.SuppressLint
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
import com.wms.android.logic.model.TaskDetail
import com.wms.android.logic.model.VoucherDetail
import com.wms.android.ui.outstock.OutstockActivity
import com.wms.android.ui.outstock.OutstockQueryActivity


class OutstockAdapter(val taskno:String,val list: List<TaskDetail>) : RecyclerView.Adapter<OutstockAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lbl_materialno: TextView = view.findViewById(R.id.lbl_materialno)
        val lbl_materialname: TextView = view.findViewById(R.id.lbl_materialname)
        val lbl_qty: TextView = view.findViewById(R.id.lbl_qty)
        val lbl_scanqty: TextView = view.findViewById(R.id.lbl_scanqty)
        val bar: ProgressBar = view.findViewById(R.id.bar)
        val lbl_bar: TextView = view.findViewById(R.id.lbl_bar)
        val linear: LinearLayout = view.findViewById(R.id.linear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_outstock, parent, false)
        //点击事件注册
        val viewHolder = ViewHolder(view)
        viewHolder.linear.setOnClickListener{
            val position = viewHolder.adapterPosition
            val item = list[position]
            if(item.scanqty==null||item.scanqty==0.0)return@setOnClickListener
            val intent = Intent(BaseApplication.context, OutstockQueryActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("item",item)
            intent.putExtra("taskno",taskno)
            BaseApplication.context.startActivity(intent)
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
        holder.lbl_qty.text ="任务总数:" + item.qty.toString()
        holder.lbl_scanqty.text ="已出库数:" + item.scanqty
        val num:Double
        try {
            num = Math.ceil(item.scanqty!!/item.qty!!*100)
            holder.lbl_bar.text = num.toInt().toString()+"%"
            when{
                num>100 -> {
                    holder.lbl_scanqty.setTextColor(Color.rgb(60,158,255))
//                    val d = ClipDrawable(
//                        ColorDrawable(Color.rgb(60,158,255)),
//                        Gravity.LEFT,
//                        ClipDrawable.HORIZONTAL
//                    )
//                    holder.bar.progressDrawable = d
                }
                num==100.0 -> {
                    holder.lbl_scanqty.setTextColor(Color.rgb(19,206,102))
                }
                num<50 ->{
                    holder.lbl_scanqty.setTextColor(Color.rgb(245,108,108))
                }
                num>=50 ->{
                    holder.lbl_scanqty.setTextColor(Color.rgb(230,162,60))
                }
                else->{

                }
            }
            holder.bar.progress =num.toInt()
        }catch(e:Exception){}
    }

}