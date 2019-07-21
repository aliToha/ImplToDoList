package com.muthohhari.todolist.model

data class Todo(
    val id: Long?, val user_name: String?,val todo_title: String?, val description:String
) {
    companion object {
        const val TODO_TABLE: String = "TODO_TABLE"
        const val ID: String = "ID_"
        const val USER_NAME: String = "USER_NAME"
        const val TODO_TITLE: String = "TODO_TITLE"
        const val DESCRIPTION: String = "DESCRIPTION"
    }
}