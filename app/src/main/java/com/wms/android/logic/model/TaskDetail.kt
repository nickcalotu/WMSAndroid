package com.wms.android.logic.model

import java.io.Serializable

class TaskDetail:Serializable {
    var id: Int = 0
    var headid: Int? = null
    var materialno: String? = null

    var materialname: String? = null
    var qty: Double? = null

    var unit: String? = null


    var rate: Double? = null
    var scanqty: Double? = null


}