package danu.ga.ui.ui.gallery.adapter

import com.google.gson.annotations.SerializedName

data class AdapterResponse(
    @field:SerializedName("Nama")
    val nama:String ?= null
)
