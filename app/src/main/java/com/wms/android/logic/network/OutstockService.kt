package com.wms.android.logic.network

import com.wms.android.logic.model.*
import retrofit2.Call
import retrofit2.http.*

interface OutstockService {


    @GET("task/getTasks")
    fun getTasks(@Query("taskno") taskno: String): Call<ResultOne<Task>>


    @GET("task/getTaskDetails")
    fun getTaskDetails(@Query("taskid") taskid: Int): Call<ResultOne<TaskDetail>>


    @GET("send/scanBarcode")
    fun scanBarcode(
        @Query("taskid") taskid: Int,
        @Query("serialno") serialno: String,
        @Query("sendtype") sendtype: String,
        @Query("zeroqty") zeroqty: Double,
        @Query("username") username: String
    ): Call<ResultTwo<Stock, TaskDetail>>


    @GET("task/btnPost")
    fun btnPost(@Query("taskid") taskid:Int):Call<ResultOne<String>>

    @GET("task/PDAGetTasktransByTaskno")
    fun PDAGetTasktransByTaskno(@Query("taskno") taskno:String,
                                @Query("materialno") materialno:String):Call<ResultOne<Tasktrans>>

    @GET("task/RollbackTaskTrans")
    fun RollbackTaskTrans(@Query("tasktransid") tasktransid:Int,
                                @Query("username") username:String):Call<ResultOne<String>>

}