package com.example.mstartkotlintask_hussam.model.beans.converterResults

import com.google.gson.annotations.SerializedName

data class Designation (
    @SerializedName("abbreviated")
    val abbreviated: String,
    @SerializedName("expanded")
    val expanded: String
    )