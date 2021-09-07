package com.wms.android.logic.network

import com.wms.android.logic.model.ResultOne
import com.wms.android.logic.model.Token
import com.wms.android.logic.model.User
import retrofit2.Call
import retrofit2.http.*

interface TestService {


    @GET("test/getNowVersion")
    fun getNowVersion():Call<ResultOne<Double>>
}