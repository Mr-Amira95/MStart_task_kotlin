package com.example.mstartkotlintask_hussam.model.beans.converterResults

import com.google.gson.annotations.SerializedName

data class Date (
    @SerializedName("hijri")
    val hijri: Hijri,
    @SerializedName("gregorian")
    val gregorian: Hijri
    )