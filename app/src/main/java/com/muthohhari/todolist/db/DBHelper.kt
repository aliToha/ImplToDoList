package com.muthohhari.todolist.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.muthohhari.todolist.model.Todo
import org.jetbrains.anko.db.*

class DbHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "Todo.db", null, 1) {
    companion object {
        private var instances: DbHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DbHelper {
            if (instances == null) {
                instances = DbHelper(ctx.applicationContext)
            }
            return instances as DbHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        //create table
        db.createTable(
            Todo.TODO_TABLE, true,
            Todo.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            Todo.USER_NAME to TEXT,
            Todo.TODO_TITLE to TEXT,
            Todo.DESCRIPTION to TEXT
        )

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(Todo.TODO_TABLE, true)
    }

}

val Context.database: DbHelper
    get() = DbHelper.getInstance(applicationContext)