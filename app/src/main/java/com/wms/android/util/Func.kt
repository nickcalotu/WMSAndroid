package com.wms.android.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.wms.android.base.ActivityCollector
import com.wms.android.base.BaseApplication
import com.wms.android.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_instock.*

//获取焦点
//扩展函数方法，在方法前加上类EditText，this代表EditText对象，就不用写接接收参数了
fun EditText.focus() {
    this.requestFocus()
    this.selectAll()
}


/**
 * 获取版本号
 */

fun getVersion(activity: Activity): Double {
    return  activity.packageManager.getPackageInfo(activity.packageName, 0).versionName.toDouble()
}

fun hideKeyBoard(window:Window) {
    val imm: InputMethodManager = BaseApplication.context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    val v: View = window.peekDecorView()
    if (null != v) {
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }
}




