package com.devahmed.topito.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Place(
    var id: String,
    var title: String,
    var description: String,
    var rate: Float,
    var address: String,
    var location: String?,
    var phones: List<String>?,
    var tags:List<String>?,
    var image:String?,
) : Parcelable {

    constructor() : this(
        id = "",
        title = "",
        description = "",
        rate = 4.5f,
        address = "",
        location = "",
        phones = listOf(),
        tags = listOf(),
        image = "",
    )

}
