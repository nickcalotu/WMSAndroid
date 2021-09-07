package com.wms.android.logic.network

import com.wms.android.logic.model.*
import retrofit2.Call
import retrofit2.http.*

interface AdjustService {

    @POST("stockadjust/btnModify")
    fun btnModify(@Body data: ArrayList<Stock> , @Query("username") username:String?):Call<ResultOne<String>>

    @POST("stockadjust/btnSend")
    fun btnSend(@Body data: Stock, @Query("username") username:String?):Call<ResultOne<String>>

    @GET("stockadjust/scanBarcode")
    fun scanBarcode(@Query("serialno") serialno:String):Call<ResultOne<Stock>>
}