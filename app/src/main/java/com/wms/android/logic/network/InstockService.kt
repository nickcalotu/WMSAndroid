package com.wms.android.logic.network

import com.wms.android.logic.model.*
import retrofit2.Call
import retrofit2.http.*

interface InstockService {

    @POST("receive/scanBarcode")
    fun scanBarcode(@Body data: Tasktrans , @Query("userName") userName:String?):Call<ResultOne<OutboxBarcode>>

    @GET("receive/scanArea")
    fun scanArea(@Query("areano") areano:String):Call<ResultOne<Area>>

    @GET("receive/getVoucher")
    fun getVoucher(@Query("serialno") serialno:String):Call<ResultTwo<Voucher, VoucherDetail>>

    @GET("voucher/btnPost")
    fun btnPost(@Query("voucherid") voucherid:Int):Call<ResultOne<String>>
}