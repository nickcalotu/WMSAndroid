package com.wms.android.util

import com.wms.android.base.BaseApplication

var shared_username=""
var shared_url=""
var shared_port =0

fun getSharedData(){
    val prefs = BaseApplication.context.getSharedPreferences("data",0)
    shared_username = prefs.getString("username","")!!
    shared_url = prefs.getString("url","")!!
    shared_port = prefs.getInt("port",8080)
}

fun setSharedData(){
    val prefs = BaseApplication.context.getSharedPreferences("data",0).edit()
    prefs.putString("username", shared_username)
    prefs.putString("url",shared_url)
    prefs.putInt("port",shared_port)
    prefs.apply()
}