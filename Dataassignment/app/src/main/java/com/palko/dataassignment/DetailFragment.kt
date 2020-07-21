package com.palko.dataassignment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import com.google.gson.internal.GsonBuildConfig
import com.squareup.okhttp.Callback
import com.squareup.okhttp.HttpUrl
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_detail.*
import java.io.IOException

class DetailFragment : Fragment() {
    private val TAG = "DetailFragment"
    var user_id : Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user_id = arguments?.getInt("user_id")

        val fragmentManager = parentFragmentManager
        Log.d(TAG, fragmentManager.backStackEntryCount.toString())
        if (!user_id.toString().equals("null")){
            getUserDetail(user_id!!)
        }

        detail_srl_refresh.setOnRefreshListener {
            getUserDetail((user_id!!))
        }
    }

    fun getUserDetail(userId: Int){
        detail_srl_refresh.isRefreshing = true
        val urlBuilder = HttpUrl.parse("https://reqres.in/api/users/"+userId.toString()).newBuilder()
        val urlString = urlBuilder.build().toString()

        val request = Request.Builder()
            .url(urlString)
            .build()

        (activity as MainActivity).client.newCall(request).enqueue(object: Callback{
            override fun onFailure(request: Request?, e: IOException?) {
                detail_srl_refresh.isRefreshing = false
                detail_srl_refresh.visibility = View.VISIBLE
                detail_pb_loadingAnimation.visibility = View.GONE
                e?.printStackTrace()
            }

            override fun onResponse(response: Response?) {
                val stringData = response?.body()?.string()
                println(stringData)
                val gson = GsonBuilder().create()

                val body = gson.fromJson(stringData, SingleUserData::class.java)
                activity?.runOnUiThread {
                    detail_srl_refresh.isRefreshing = false
                    detail_pb_loadingAnimation.visibility = View.GONE
                    detail_srl_refresh.visibility = View.VISIBLE


                    detail_tv_name.text = body.data.first_name+" "+body.data.last_name
                    detail_tv_email.text = body.data.email
                    Glide.with(this@DetailFragment).load(body.data.avatar).into(detail_iv_avatar)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy was called.")
    }


}