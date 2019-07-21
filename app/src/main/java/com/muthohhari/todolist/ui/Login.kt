package com.muthohhari.todolist.ui


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.muthohhari.todolist.OpenFragment

import com.muthohhari.todolist.R
import kotlinx.android.synthetic.main.fragment_login.*


class Login : Fragment(),View.OnClickListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val _RcSignIn = 101
    private var openFragment: OpenFragment? = null

    fun setOpenFragment(openFragment: OpenFragment) {
        this.openFragment = openFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createAccountText.setOnClickListener(this)
        loginGoogleButton.setOnClickListener(this)
        loginPasswordButton.setOnClickListener(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context!!, gso)
        mAuth = FirebaseAuth.getInstance()
    }
    override fun onClick(view: View) {
        val id = view.id
        when(id){
            R.id.createAccountText -> moveSignUp()
            R.id.loginPasswordButton -> signIn(editTextEmail.text.toString(), editTextPassword.text.toString())
            R.id.loginGoogleButton -> googleSignIn()
        }
    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, _RcSignIn)
    }

    private fun moveSignUp() {
        openFragment?.openFragment(R.string.signup)
    }

    private fun signIn(email: String, password: String) {
        if (!validateForm(email, password)) {
            return
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                moveTodolist()
            } else {
                Toast.makeText(context,getString(R.string.filed_login),Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun moveTodolist() {
        openFragment?.openFragment(R.string.main)
    }

    private fun validateForm(email: String, password: String): Boolean {
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
        }else{
            inputEmail.helperText = " "
            inputPassword.helperText = " "
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == _RcSignIn) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w("TAG", "Google sign in failed", e)
                Toast.makeText(context,getString(R.string.filed_login),Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + account.id!!)
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("TAG", "signInWithCredential:success")
                    moveTodolist()
                } else {
                    Log.w("TAG", "signInWithCredential:failure", it.exception)
                    Toast.makeText(context,getString(R.string.filed_login),Toast.LENGTH_SHORT).show()
                }

                // ...
            }

    }
}
