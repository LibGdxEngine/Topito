package com.devahmed.topito.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tag(val id:String, var title:String) : Parcelable{
    constructor() : this(id="", title="")
}
