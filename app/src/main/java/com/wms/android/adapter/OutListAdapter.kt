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
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.wms.android.MainActivity
import com.wms.android.R
import com.wms.android.base.BaseApplication
import com.wms.android.logic.model.Task
import com.wms.android.logic.model.VoucherDetail
import com.wms.android.ui.instock.InstockActivity
import com.wms.android.ui.outstock.OutstockActivity
import com.wms.android.ui.tray.DownTrayActivity
import com.wms.android.ui.tray.UpTrayActivity
import com.wms.android.util.showToast
import java.util.ArrayList


class OutListAdapter(val list: List<Task>) : RecyclerView.Adapter<OutListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lbl_taskno: TextView = view.findViewById(R.id.lbl_taskno)
        val lbl_createtime: TextView = view.findViewById(R.id.lbl_createtime)
        val lbl_creator: TextView = view.findViewById(R.id.lbl_creator)
        val lbl_customername: TextView = view.findViewById(R.id.lbl_customername)
        val linear:LinearLayout= view.findViewById(R.id.linear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_outlist, parent, false)

        //点击事件注册
        val viewHolder = ViewHolder(view)
        viewHolder.linear.setOnClickListener{
            val position = viewHolder.adapterPosition
            val item = list[position]
            val intent = Intent(BaseApplication.context,OutstockActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("taskid", item.id)
            intent.putExtra("taskno",item.taskno)
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
        holder.lbl_taskno.text = "单号:" + item.taskno
        holder.lbl_taskno.setTextColor(Color.rgb(60,158,255))
        holder.lbl_createtime.text ="创建日期:" + item.createtime!!.split(" ")[0]
        holder.lbl_creator.text ="创建人:" + item.creator
        holder.lbl_customername.text ="客户:" + item.customername
    }

}