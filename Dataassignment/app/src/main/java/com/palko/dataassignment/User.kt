package com.palko.dataassignment

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (val id:Int, val email: String, val first_name: String,val  last_name: String, val avatar: String):
    Parcelable {
}