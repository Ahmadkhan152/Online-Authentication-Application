package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R.id.btnGoogleSignin
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignIn.*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences;
    lateinit var callbackManager: CallbackManager;
    lateinit var fbLoginButton:LoginButton;
    lateinit var mGoogleSignInClient:GoogleSignInClient;
    val RC_SIGN_IN=100;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FacebookSdk.fullyInitialize()
        AppEventsLogger.activateApp(application);
        callbackManager = CallbackManager.Factory.create();
        getSupportActionBar()?.setTitle("LOGIN")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        sharedPreferences = getSharedPreferences("UserCheck", MODE_PRIVATE)
        val dataEditor: SharedPreferences.Editor = sharedPreferences.edit()
        val logInCheck: Boolean = sharedPreferences.getBoolean("UserBox", false)
        fbLoginButton = findViewById<View>(R.id.tvFacebookBtn) as LoginButton
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = getClient(this, gso);
        val account = getLastSignedInAccount(this)
        findViewById<View>(btnGoogleSignin).setOnClickListener {
            signIn()
        };
        //facebookIntegration();
        if (logInCheck) {
            startActivity(Intent(this, GooglemapActivity::class.java))
            finish()
        }
        var cbCheckBoxForLogIn: CheckBox = findViewById(R.id.cbLogIn)
        var etEmailAddressOfUser: EditText = findViewById(R.id.etUserName)
        var etPasswordOfUser: EditText = findViewById(R.id.etPassword)
        val signUp: TextView = findViewById(R.id.tvSignUp)
        val logIn: TextView = findViewById(R.id.tvLogIn)
        signUp.setOnClickListener({
            val shifterActivity: Intent = Intent(this, SignupActivity::class.java)
            startActivity(shifterActivity)
        })
        logIn.setOnClickListener {
            if (cbCheckBoxForLogIn.isChecked()) {
                var bool: Boolean = true
                dataEditor.putBoolean("UserBox", bool).commit()
            }
            val emailAddressOfUser: String = etEmailAddressOfUser.text.toString()
            val passwordOfUser: String = etPasswordOfUser.text.toString()
            if (emailAddressOfUser.length != null && passwordOfUser.length != null) {
                dataEditor.putString("User_Name", emailAddressOfUser).commit()
                var auth: FirebaseAuth
                auth = Firebase.auth
                auth.signInWithEmailAndPassword(emailAddressOfUser, passwordOfUser)
                    .addOnCompleteListener(this)
                    { task ->
                        if (task.isSuccessful) {
                            val shifter: Intent = Intent(this, GooglemapActivity::class.java)
                            startActivity(shifter)
                        } else {
                            Toast.makeText(
                                baseContext, task.exception.toString(), Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
//    {
//        callbackManager.onActivityResult(requestCode, resultCode, data)
//        super.onActivityResult(requestCode, resultCode, data)
//    }
    fun facebookIntegration(){
        fbLoginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
            }
            override fun onError(exception: FacebookException) {
            }
            override fun onSuccess(result: LoginResult) {
                Toast.makeText(this@LoginActivity,"Log In Successfully",Toast.LENGTH_LONG).show()
                if (result!=null)
                {
                    val graphRequest:GraphRequest= GraphRequest.newMeRequest(result.accessToken){ obj, response ->
                        if (obj!=null)
                        {
                            val profileId:String=obj.getString("id")
                            val userName:String=obj.getString("name")
                            val shiftActivity:Intent=Intent(this@LoginActivity,FacebookloginActivity::class.java)
                            shiftActivity.putExtra(TAG_USER1,userName)
                            shiftActivity.putExtra(TAG_USER2,profileId)
                            startActivity(shiftActivity)
                        }
                    }
                    val bundle:Bundle= Bundle()
                    bundle.putString("fields","id,name,email,gender,birthday")
                    graphRequest.parameters.getBundle(bundle.toString())
                    graphRequest.executeAsync()
                }
            }

        })
    }
    private fun signIn()
    {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //callbackManager.onActivityResult(requestCode, resultCode, data)
        //super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN)
        {
            val task:Task<GoogleSignInAccount> = getSignedInAccountFromIntent(data);
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>)
    {
            val account = completedTask.getResult(ApiException::class.java)
            val acct = getLastSignedInAccount(this@LoginActivity)
            if (acct != null) {
                val personName = acct.displayName
                val personGivenName = acct.givenName
                val personFamilyName = acct.familyName
                val personEmail = acct.email
                val personId = acct.id
                val personPhoto: Uri? = acct.photoUrl
                Toast.makeText(this@LoginActivity,"Sign In",Toast.LENGTH_SHORT).show()
                val shiftActivity:Intent=Intent(this@LoginActivity,GoogleloginActivity::class.java)
                shiftActivity.putExtra(TAG_EMAIL,personEmail)
                shiftActivity.putExtra(TAG_NAME,personName)
                startActivity(shiftActivity)
                finish()
            }
    }
}