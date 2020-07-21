package com.palko.dataassignment

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (val id:Int, val email: String, val first_name: String,val  last_name: String, val avatar: String):
    Parcelable {

   // var id: Int = 0
  //  @SerializedName("email")
   // var email: String = ""
  //  @SerializedName("first_name")
  //  var first_name: String = ""
  //  @SerializedName("last_name")
  //  var last_name: String = ""
   // @SerializedName("avatar")
   // var avatar: String = ""

}