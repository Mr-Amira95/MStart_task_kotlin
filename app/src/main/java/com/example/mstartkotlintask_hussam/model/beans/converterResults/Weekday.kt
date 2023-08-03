package com.example.mstartkotlintask_hussam.model.beans.converterResults

import com.google.gson.annotations.SerializedName

data class Weekday (
    @SerializedName("en")
    val en: String,
    @SerializedName("ar")
    val ar: String
    )