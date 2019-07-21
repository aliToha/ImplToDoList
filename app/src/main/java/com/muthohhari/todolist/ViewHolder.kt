package com.muthohhari.todolist

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.muthohhari.todolist.model.Todo

class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
        private val title = view.findViewById<TextView>(R.id.title_todo)

        fun bindItem(items: Todo, clickListener: (Todo) -> Unit) {
            title.text = items.todo_title
            itemView.setOnClickListener { clickListener(items) }
        }
}