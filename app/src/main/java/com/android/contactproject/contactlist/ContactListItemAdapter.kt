package com.android.contactproject.contactlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.contactproject.AddMemberData
import com.android.contactproject.databinding.GridTypeItem2Binding

class ContactListItemAdapter(private val list: ArrayList<AddMemberData>): RecyclerView
.Adapter<RecyclerView.ViewHolder>() {
    inner class ViewHolder(private val binding: GridTypeItem2Binding): RecyclerView.ViewHolder
        (binding.root){
            fun bind(item:AddMemberData){
                binding.apply {
                    GridProfile2.setImageURI(item.profile)
                    GridName2.text = item.name
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = GridTypeItem2Binding.inflate(LayoutInflater.from(parent.context),parent,
            false)
       return ViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        val item = list[position]
        holder.bind(item)
    }

}