package com.wms.android.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.wms.android.ui.login.LoginActivity
import com.wms.android.util.showAlertDialog

open class BaseActivity : AppCompatActivity() {

    //强制下线接收器
    lateinit var receiver:ForeceOfflineReceiver

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        ActivityCollector.addActivity(this)
        //隐藏自带的标题栏
        //supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()

        //界面在顶层时注册
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.wms.android.FORCE_OFFLINE")
        receiver = ForeceOfflineReceiver()
        registerReceiver(receiver,intentFilter)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    override fun onPause() {
        super.onPause()

        //界面不在顶层时取消注册
        unregisterReceiver(receiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    inner class ForeceOfflineReceiver :BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent?) {
            showAlertDialog(context,"Warning","Yuo are forced to be offline.Please try to login again."){
                ActivityCollector.finishAll()//销毁所有Activit
                val i = Intent(context,LoginActivity::class.java)
                context.startActivity(i)//重新启动登录界面
            }
        }

    }
}