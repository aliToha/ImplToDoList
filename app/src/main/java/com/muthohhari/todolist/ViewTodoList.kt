package com.muthohhari.todolist

import com.muthohhari.todolist.model.Todo

interface ViewTodoList {
    fun showLoading()
    fun hideLoading()
    fun empty()
    fun refresh()
    fun showList(data: List<Todo>)
}