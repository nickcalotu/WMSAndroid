package com.wms.android.logic.network

import com.wms.android.logic.model.*
import retrofit2.Call
import retrofit2.http.*

interface MoveService {

    @GET("move/scanBarcode")
    fun scanBarcode( @Query("toareano") toareano:String?,
                     @Query("serialno") serialno:String?,
                     @Query("movetype") movetype:String?,
                     @Query("username") username:String?
    ):Call<ResultOne<Stock>>

    @GET("move/scanArea")
    fun scanArea(@Query("areano") areano:String):Call<ResultOne<Area>>

}