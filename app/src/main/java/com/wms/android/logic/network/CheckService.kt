package com.wms.android.logic.network

import com.wms.android.logic.model.*
import retrofit2.Call
import retrofit2.http.*

interface CheckService {

    @GET("check/setCheckStatusStart")
    fun setCheckStatusStart(@Query("checkno") checkno:String): Call<ResultOne<Check>>

    @GET("check/getChecks")
    fun getChecks(@Query("checkno") checkno:String):Call<ResultOne<Check>>


    @GET("checkdetail/scanArea")
    fun scanArea(@Query("checkno") checkno:String,@Query("areano") areano:String):Call<ResultOne<CheckArea>>

    @GET("checkdetail/scanBarcode")
    fun scanBarcode(@Query("serialno") serialno:String):Call<ResultOne<Stock>>

    @GET("checkdetail/getCheckDetailsGroup")
    fun getCheckDetailsGroup(@Query("checkno") checkno:String):Call<ResultOne<CheckDetail>>

    @GET("checkdetail/delCheckDetail")
    fun delCheckDetail(@Query("checkno") checkno:String,
                       @Query("areano") areano:String,
                       @Query("materialno") materialno:String):Call<ResultOne<CheckDetail>>

    @GET("checkdetail/btnSubmit")
    fun btnSubmit(@Query("checkno") checkno:String,
                  @Query("areano") areano:String,
                  @Query("serialno") serialno:String,
                  @Query("scanqty") scanqty:Double,
                  @Query("username") username:String
    ):Call<ResultOne<CheckDetail>>

}