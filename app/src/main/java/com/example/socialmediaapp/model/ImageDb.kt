package com.example.socialmediaapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ImageDb(val url:String,val timestamp:Long,val description:String):Parcelable{
    constructor():this("",0,"")
}