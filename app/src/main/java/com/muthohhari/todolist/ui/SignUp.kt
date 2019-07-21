package com.muthohhari.todolist.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.muthohhari.todolist.OpenFragment
import com.muthohhari.todolist.R
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUp : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private var openFragment: OpenFragment? = null

    fun setOpenFragment(openFragment: OpenFragment) {
        this.openFragment = openFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        createAccountButton.setOnClickListener {
            createAccount(
                editTextEmail.text.toString(),
                editTextUsername.text.toString(),
                editTextPassword.text.toString()
            )
        }
    }

    private fun createAccount(email: String, user: String, password: String) {
        if (!validateForm(user, email, password)) {
            return
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val current = mAuth.currentUser
                val updates = UserProfileChangeRequest.Builder()
                    .setDisplayName(user).build()
                current?.updateProfile(updates)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        FirebaseAuth.getInstance().signOut()
                        Toast.makeText(context, R.string.success_create_account, Toast.LENGTH_SHORT).show()
                        openFragment?.openFragment(R.string.login)
                    } else {
                        Log.w("TAG", "failed to create account")
                        Toast.makeText(context, R.string.text_already_have_account, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Log.w("TAG", "failed to create account")
                Toast.makeText(context, R.string.text_already_have_account, Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun validateForm(userName: String, email: String, password: String): Boolean {
        if (TextUtils.isEmpty(userName)) {
            inputUsername.helperText = "Enter User Name"
            return false
        }
        if (TextUtils.isEmpty(email)) {
            inputEmail.helperText = "Enter Email"
            return false
        }
        if (TextUtils.isEmpty(password)) {
            inputPassword.helperText = "Enter Password"
            return false
        }
        if (password.length < 6) {
            inputPassword.helperText = "Password to short, enter minimum 6 characters"
            return false
        } else {
            inputUsername.helperText = " "
            inputEmail.helperText = " "
            inputPassword.helperText = " "
        }
        return true
    }
}
