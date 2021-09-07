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
import com.wms.android.logic.model.Check
import com.wms.android.logic.model.Task
import com.wms.android.logic.model.VoucherDetail
import com.wms.android.ui.check.CheckActivity
import com.wms.android.ui.instock.InstockActivity
import com.wms.android.ui.outstock.OutstockActivity
import com.wms.android.ui.tray.DownTrayActivity
import com.wms.android.ui.tray.UpTrayActivity
import com.wms.android.util.getCheckStatus
import com.wms.android.util.showToast
import java.util.ArrayList


class CheckListAdapter(val list: List<Check>) : RecyclerView.Adapter<CheckListAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lbl_checkno: TextView = view.findViewById(R.id.lbl_checkno)
        val lbl_createtime: TextView = view.findViewById(R.id.lbl_createtime)
        val lbl_remark: TextView = view.findViewById(R.id.lbl_remark)
        val lbl_status:TextView = view.findViewById(R.id.lbl_status)
        val linear:LinearLayout= view.findViewById(R.id.linear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_checklist, parent, false)

        //点击事件注册
        val viewHolder = ViewHolder(view)
        viewHolder.linear.setOnClickListener{
            val position = viewHolder.adapterPosition
            val item = list[position]
            val intent = Intent(BaseApplication.context, CheckActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("checkno",item.checkno)
            intent.putExtra("status",item.status)
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
        holder.lbl_checkno.text = "单号:" + item.checkno
        holder.lbl_checkno.setTextColor(Color.rgb(60,158,255))
        holder.lbl_createtime.text ="创建日期:" + item.createtime!!.split(" ")[0]
        //{1：新建，2：开始，3：完成，4：终止}
        holder.lbl_status.text =getCheckStatus(item.status!!)
        holder.lbl_status.setTextColor(Color.rgb(19,206,102))
        holder.lbl_remark.text ="备注:" + item.remark
    }

}