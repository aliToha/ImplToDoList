package com.muthohhari.todolist

import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}
fun Fragment.toast(msg: String) {
    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
}