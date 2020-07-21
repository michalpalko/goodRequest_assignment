package com.palko.dataassignment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list.view.*

class UsersAdapter(val users: ArrayList<User>) : RecyclerView.Adapter<ItemViewHolder>(){

    override fun getItemCount(): Int {
        return users.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.item_list,parent, false)
        return ItemViewHolder(item)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.view.item_tv_id.text = users[position].id.toString()
        holder.view.item_tv_name.text = users[position].first_name
        holder.view.item_tv_surname.text = users[position].last_name

        holder.view.setOnClickListener {
            val bundle = bundleOf("user_id" to users[position].id)
            holder.view.findNavController().navigate(R.id.action_listFragment_to_detailFragment, bundle)
        }
    }
}

class ItemViewHolder(val view: View): RecyclerView.ViewHolder(view){

}
