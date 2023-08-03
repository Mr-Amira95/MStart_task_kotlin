package com.example.mstartkotlintask_hussam.Controller.Network

import com.example.mstartkotlintask_hussam.model.beans.converterResults.ConverterResults
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DateService {
    @GET("gToH")
    fun convertDate(@Query("date") date: String): Call<ConverterResults>

}