package com.palko.dataassignment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.GsonBuilder
import com.squareup.okhttp.Callback
import com.squareup.okhttp.HttpUrl
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import kotlinx.android.synthetic.main.fragment_detail.*
import java.io.IOException


class DetailFragment : Fragment() {
    private val TAG = "DetailFragment"
    var user_id : Int? = 0
    private lateinit var requestOptions: RequestOptions

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

        requestOptions = RequestOptions()
            .centerCrop()
            .placeholder(requireContext().resources.getDrawable(R.drawable.animation_glide_loading, requireContext().theme))
            .error(requireContext().resources.getDrawable(R.drawable.ic_launcher_background,requireContext().theme))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
            .dontAnimate()
            .dontTransform()

        if (!user_id.toString().equals("null")){
            getUserDetail(user_id!!)
        }

        detail_srl_refresh.setOnRefreshListener {
            getUserDetail(user_id!!)
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
                Log.i(TAG, "Response failure..")

                runLoadUi()
                e?.printStackTrace()
            }

            override fun onResponse(response: Response?) {
                Log.i(TAG, "Response is successful.")

                val stringData = response?.body()?.string()
                println(stringData)
                val gson = GsonBuilder().create()

                val body = gson.fromJson(stringData, SingleUserData::class.java)
                activity?.runOnUiThread {
                    runLoadUi()
                    val resources = requireContext().resources
                    val name = String.format(resources.getString(R.string.detail_name, body.data.first_name, body.data.last_name))
                    detail_tv_name.text = name
                    detail_tv_email.text = body.data.email
                    Glide.with(this@DetailFragment)
                        .load(body.data.avatar)
                        .apply(requestOptions)
                        .into(detail_iv_avatar)
                }
            }
        })
    }

    fun runLoadUi(){
        detail_srl_refresh.isRefreshing = false
        detail_pb_loadingAnimation.visibility = View.GONE
        detail_srl_refresh.visibility = View.VISIBLE
    }



}