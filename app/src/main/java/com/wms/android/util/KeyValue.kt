package com.wms.android.util

fun getCheckStatus(status:Int)=when(status){
    1->"新建"
    2->"开始"
    3->"完成"
    4->"终止"
    else->"新建"
}