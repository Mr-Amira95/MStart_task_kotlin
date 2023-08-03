package com.example.mstartkotlintask_hussam.model.beans.converterResults

import com.google.gson.annotations.SerializedName

data class ConverterResults (
    @SerializedName("code")
    val code: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: Date
    )
