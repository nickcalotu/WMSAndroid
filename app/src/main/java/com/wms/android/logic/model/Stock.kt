package com.wms.android.logic.model

class Stock:Cloneable {
    var id: Int = 0
    var serialno: String? = null
    var materialno: String? = null
    var materialname: String? = null
    var supplierno: String? = null
    var suppliername: String? = null
    var unit: String? = null
    var qty: Double? = null
    var outpackage: Double? = null
    var batchno: String? = null
    var probatchno: String? = null
    var validtime: String? = null
    var standard: String? = null
    var areano:String ? =null
    var warehouseno:String ? =null
    var trayno: String? = null
    var status: Int? = null
    var creator: String? = null
    var createtime: String? = null
    var message:String? = null

    @Throws(CloneNotSupportedException::class)// 克隆失败抛出异常
    override public fun clone(): Stock{
        return super.clone() as Stock // 类型强制转换
    }
}