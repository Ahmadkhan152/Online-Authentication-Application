package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task

class GoogleloginActivity : AppCompatActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_googlelogin)
        val tvUserEmailOfGoogle: TextView =findViewById(R.id.tvUserEmailOfGoogle)
        val tvUserNameOfGoogle: TextView =findViewById(R.id.tvUserNameOfGoogle)
        val btnSignoutGoogle:Button=findViewById(R.id.btnGoogleSignout)
        val emailAddress: String = intent.getStringExtra(TAG_EMAIL).toString()
        val userName: String = intent.getStringExtra(TAG_NAME).toString()
        tvUserEmailOfGoogle.text = "Email Address: "+emailAddress
        tvUserNameOfGoogle.text = "Username: "+userName
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btnSignoutGoogle.setOnClickListener {
            signOut()
            startActivity(Intent(this@GoogleloginActivity, LoginActivity::class.java))
            finish()
        }
    }
    fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener{
                @Override
                fun onComplete(task:Task<Void>) {
                    Toast.makeText(this@GoogleloginActivity,"Sign Out",Toast.LENGTH_SHORT).show()
                }
            };
    }
    fun revokeAccess() {
        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener {
                @Override
                fun onComplete(task:Task<Void>) {
                    // ...
                }
            };
    }

}