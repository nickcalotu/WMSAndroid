package com.wms.android.util

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.wms.android.R
import com.wms.android.base.ActivityCollector
import com.wms.android.base.BaseApplication
import com.wms.android.ui.login.LoginActivity

fun showToast(text:String,duration:Int = Toast.LENGTH_SHORT){
    Toast.makeText(BaseApplication.context,text,duration).show()
}

fun showSnackbar(view: View, text:String, actionText:String ?= null, duration: Int = Snackbar.LENGTH_SHORT,block:(()->Unit) ?= null){
    val snackbar = Snackbar.make(view,text,duration)
    if(actionText!=null && block!=null){
        snackbar.setAction(actionText){
            block()
        }
    }
    snackbar.show()
}

fun showAlertDialog(context: Context, title:String, text:String,
                    textOK:String = "确认", textCancel:String = "取消",
                    cancelable:Boolean = true,
                    block:(()->Unit) ?= null){
    AlertDialog.Builder(context, R.style.dialogAlert).apply {
        setTitle(title)
        setMessage(text)
        setCancelable(cancelable)//对话框不能取消
        if(cancelable){
            setNegativeButton(textCancel){_,_->}
        }
        if(block!=null){
            setPositiveButton(textOK){_,_->
                block()
            }
        }
        show()
    }
}



