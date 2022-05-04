package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class FacebookloginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebooklogin)

        val tvUserIDOfFacebook:TextView=findViewById(R.id.tvUserIDOfFacebook)
        val tvUserNameOfFacebook:TextView=findViewById(R.id.tvUserNameOfFacebook)
        val userProfileID: String = intent.getStringExtra(TAG_USER2).toString()
        val userName: String = intent.getStringExtra(TAG_USER1).toString()
        tvUserIDOfFacebook.text = "Your User Profile ID: "+userProfileID
        tvUserNameOfFacebook.text = "Your Username: "+userName
    }
}