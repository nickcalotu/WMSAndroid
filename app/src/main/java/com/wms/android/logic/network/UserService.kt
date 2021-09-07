package com.wms.android.logic.network

import com.wms.android.logic.model.ResultOne
import com.wms.android.logic.model.Token
import com.wms.android.logic.model.User
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @POST("user/login")
    fun login(@Body data: User): Call<ResultOne<Token>>

    @GET("user/info")
    fun info(@Query("token") token:String):Call<ResultOne<User>>
}