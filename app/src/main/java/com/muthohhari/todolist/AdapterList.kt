package com.muthohhari.todolist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.muthohhari.todolist.model.Todo

class AdapterList (private val context: Context, private val items: List<Todo>, private val clickListener: (Todo) -> Unit) :
RecyclerView.Adapter<ViewHolder>()  {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bindItem(items[position],clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_todo_list,parent,false))


    override fun getItemCount(): Int = items.size

}