package com.example.mstartkotlintask_hussam.model.beans.converterResults

import com.google.gson.annotations.SerializedName
import java.util.Objects

data class Hijri(
    @SerializedName("date")
    val date: String,
    @SerializedName("format")
    val format: String,
    @SerializedName("day")
    val day: String,
    @SerializedName("weekday")
    val weekday: Weekday,
    @SerializedName("month")
    val month: Month,
    @SerializedName("designation")
    val designation: Designation,
    @SerializedName("holidays")
    val holidays: List<Objects>


)