package com.wms.android.logic.model

open class BaseResult(var code:Int,var message:String)

class ResultOne<T>(code:Int, message:String, var model:T, var list : ArrayList<T>,var total:Int):BaseResult(code,message)

class ResultTwo<T,K>(code:Int, message:String, var model:T, var list : ArrayList<K>,var total:Int):BaseResult(code,message)

class Token(var token:String)