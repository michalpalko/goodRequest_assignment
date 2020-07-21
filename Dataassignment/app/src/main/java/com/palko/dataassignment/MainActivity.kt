package com.palko.dataassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.okhttp.HttpUrl
import com.squareup.okhttp.OkHttpClient

class MainActivity : AppCompatActivity() {
    val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
