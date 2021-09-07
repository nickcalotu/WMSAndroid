package com.wms.android.logic.network

import com.wms.android.logic.model.OutboxBarcode
import com.wms.android.logic.model.ResultOne
import com.wms.android.logic.model.Token
import com.wms.android.logic.model.User
import retrofit2.Call
import retrofit2.http.*

interface TrayService {

    @POST("tray/trayScanOther")
    fun trayScanOther(@Body data: OutboxBarcode): Call<ResultOne<OutboxBarcode>>

    @GET("tray/trayScanFirst")
    fun trayScanFirst(@Query("serialno") serialno:String):Call<ResultOne<OutboxBarcode>>

    @GET("tray/deTrayScanFirst")
    fun deTrayScanFirst(@Query("serialno") serialno:String):Call<ResultOne<OutboxBarcode>>

    @GET("tray/deTrayOneBox")
    fun deTrayOneBox(@Query("serialno") serialno:String):Call<ResultOne<OutboxBarcode>>

    @GET("tray/deTrayAll")
    fun deTrayAll(@Query("serialno") serialno:String):Call<ResultOne<OutboxBarcode>>
}