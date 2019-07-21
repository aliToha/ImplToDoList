package com.muthohhari.todolist.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.muthohhari.todolist.OpenFragment
import com.muthohhari.todolist.R

class MainActivity : AppCompatActivity(), OpenFragment {

    private lateinit var mAuth: FirebaseAuth
    private var user: String? = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        user = mAuth.currentUser?.displayName

        object : CountDownTimer(1000, 500) {
            override fun onFinish() {
                if (currentUser != null) {
                    openFragment(R.string.main)
                } else {
                    openFragment(R.string.login)
                }
            }

            override fun onTick(p0: Long) {}
        }.start()

    }

    override fun openFragment(fragmentId: Int) {
        user = mAuth.currentUser?.displayName
        val bundel = Bundle()
        bundel.putString("USER", user)
        when (fragmentId) {
            R.string.login -> {
                val login = Login()
                login.arguments = bundel
                login.setOpenFragment(this)
                moveFragment(login, "login")
            }
            R.string.signup -> {
                val signup = SignUp()
                signup.arguments = bundel
                signup.setOpenFragment(this)
                moveFragment(signup, "signup")
            }
            R.string.main -> {
                val main = TodoList()
                main.arguments = bundel
                main.setOpenFragment(this)
                moveFragment(main, "main")
            }
        }
    }

    private fun moveFragment(fragment: Fragment, TAG: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if(TAG != "login"){
            if(TAG != "main"){
                fragmentTransaction.isAddToBackStackAllowed
                fragmentTransaction.addToBackStack(TAG)
            }
        }
        fragmentTransaction.replace(R.id.frag_container, fragment)
        fragmentTransaction.commit()
    }

}
