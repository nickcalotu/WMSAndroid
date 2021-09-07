package com.wms.android.base

import android.app.Application
import android.content.Context
import com.wms.android.logic.network.ServiceCreator
import com.wms.android.util.getSharedData
import com.wms.android.util.shared_port
import com.wms.android.util.shared_url

class BaseApplication :Application(){
    companion object{
        //设置全局context
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        //获取全部SharedPreference内容
        getSharedData()
        if(shared_port<1|| shared_port>65535)
            shared_port=8080

    }
}