package com.palko.dataassignment

import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.squareup.okhttp.Callback
import com.squareup.okhttp.HttpUrl
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import kotlinx.android.synthetic.main.fragment_list.*
import java.io.IOException


class ListFragment() : Fragment() {
    private val TAG = "ListFragment"
    private var page = 1
    private var usersArrayList = ArrayList<User>()
    private var visibleItemCount:Int=0
    private var totalItemCount:Int=0
    private var pastVisibleItems: Int=0
    private var loading=true
    private var recyclerAdapter = UsersAdapter(usersArrayList)
    private var initRequest = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState!= null) {
            usersArrayList = savedInstanceState.getParcelableArrayList<User>("users") as ArrayList<User>
            recyclerAdapter = UsersAdapter(usersArrayList)
            initRequest = savedInstanceState.getBoolean("initRequest")
        }

        if (initRequest){
            getUsers(page)
            initRequest = false
        }
        runUserFriendlyAnimation()

        val linearLayoutManager = LinearLayoutManager(activity?.applicationContext)
        list_rv_listOfData.layoutManager = linearLayoutManager
        list_rv_listOfData.adapter = recyclerAdapter
        recyclerAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        list_rv_listOfData.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (!loading && page<2) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = true
                            page = page+1
                            getUsers(page)
                        }
                    }
                }
            }
        })

        list_srl_refresh.setOnRefreshListener {
            usersArrayList.clear()
            page = 1
            getUsers(page)
        }
    }

    private fun getUsers(page: Int){
        list_srl_refresh.isRefreshing = true

        val urlBuilder = HttpUrl.parse("https://reqres.in/api/users").newBuilder()
        urlBuilder.addQueryParameter("page",page.toString())
        val urlString = urlBuilder.build().toString()

        val request = Request.Builder()
            .url(urlString)
            .build()

        (activity as MainActivity).client.newCall(request).enqueue(object : Callback{
            override fun onFailure(request: Request?, e: IOException?) {
                list_srl_refresh.isRefreshing = false
                runUserFriendlyAnimation()
                e?.printStackTrace()
            }

            override fun onResponse(response: Response?) {
                if (!response!!.isSuccessful){
                    Log.d(TAG, response.toString())
                }
                else {
                    Log.d(TAG, "Response is Succesfull.")
                    val stringData = response.body().string()
                    val gson = GsonBuilder().create()
                    val body = gson.fromJson(stringData,UsersData::class.java)

                    usersArrayList.addAll(body.data)

                    activity?.runOnUiThread {
                        list_srl_refresh.isRefreshing = false
                        runUserFriendlyAnimation()
                        recyclerAdapter.notifyDataSetChanged()
                    }
                    loading = false
                }
            }
        })
    }

    fun runUserFriendlyAnimation(){
        list_pb_loadingAnimation.visibility = View.GONE
        list_srl_refresh.visibility = View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("users", usersArrayList)
        outState.putBoolean("initRequest", initRequest)
        super.onSaveInstanceState(outState)
    }

}
