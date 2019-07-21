package com.muthohhari.todolist.presenter

import android.content.Context
import com.muthohhari.todolist.ViewTodoList
import com.muthohhari.todolist.db.database
import com.muthohhari.todolist.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class TodoListPresenter(
    private val view: ViewTodoList,
    private val context: Context
) {

    fun listActivity(userName:String) {
        view.showLoading()
        GlobalScope.launch(Dispatchers.Main) {
            var team: List<Todo>? = null
            context.database.use {
                val result = select(Todo.TODO_TABLE)
                   .whereArgs("(USER_NAME = {user_name})","user_name" to userName)
                val data = result.parseList(classParser<Todo>())
                team = data
            }
            if (team!!.isEmpty()) {
                view.hideLoading()
                view.empty()
            } else {
                view.showList(team!!)
                view.hideLoading()
            }

        }
    }

    fun saveActivity(title_todo: String, user_name: String, description:String) {
        GlobalScope.launch(Dispatchers.Main) {
            var team: Long? = null
            context.database.use {
              val insertRow=  insert(
                    Todo.TODO_TABLE,
                  Todo.USER_NAME to user_name,
                  Todo.TODO_TITLE to title_todo,
                  Todo.DESCRIPTION to description
                )
                team = insertRow
            }
            if (team != null) {
                view.refresh()
            }
        }
    }
}