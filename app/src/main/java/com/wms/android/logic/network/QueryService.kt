package com.wms.android.logic.network

import com.wms.android.logic.model.ResultOne
import com.wms.android.logic.model.Stock
import com.wms.android.logic.model.Token
import com.wms.android.logic.model.User
import retrofit2.Call
import retrofit2.http.*

interface QueryService {

    @GET("stock/hhtStockQueryByArea")
    fun hhtStockQueryByArea(@Query("areano") areano:String): Call<ResultOne<Stock>>

    @GET("stock/hhtStockQueryByMaterial")
    fun hhtStockQueryByMaterial(@Query("serialno") serialno:String):Call<ResultOne<Stock>>
}