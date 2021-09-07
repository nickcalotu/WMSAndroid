package com.wms.android.logic.model

data class User(var username:String,var password:String){
    var id:Int = 0
    var isdel:Int? = null
    var groupid:Int? = null
    var groupno:String? = null
    var groupname:String? = null
    var name:String? = null
    var sex:String? = null
    var phone:String? = null
    var createtime:String? = null
    var creator:String? = null
    var avatar:String? = null
    var introduction:String? = null
    var menunames:List<String>?=null

    //里面的内容都为静态对象和方法
    companion object{
        lateinit var user:User
    }
}