package com.example.myapplication
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit
class OTPActivity : AppCompatActivity() {
    private var authObject: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        getSupportActionBar()?.setTitle("OTP Activity")
        var etOTPOfUser:EditText=findViewById(R.id.etOTPOfUser)
        val btnSignUpOfUser:Button=findViewById(R.id.btnSignUpOfUser)
        var verificationCodeSendBySystem:String="";
        authObject = FirebaseAuth.getInstance();
        val tvOfOTP:TextView=findViewById(R.id.tvOfOTP)
        val firstName: String = getIntent().getStringExtra("first_Name").toString()
        val secondName: String = getIntent().getStringExtra("second_Name").toString()
        val gender: String = getIntent().getStringExtra("Gender").toString()
        val passWord: String = getIntent().getStringExtra("PassWord").toString()
        val userEmail: String = getIntent().getStringExtra("User_Email").toString()
        val phoneNumber: String = getIntent().getStringExtra("Number").toString()
        var Longitude = getIntent().getDoubleExtra("LongitudeLocation",897.9).toDouble()
        var Latitude = getIntent().getDoubleExtra("LatitudeLocation",789.9).toDouble()
        var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                var code:String=credential.smsCode as String
                val cre:PhoneAuthCredential=PhoneAuthProvider.getCredential(verificationCodeSendBySystem,code);
                signInWithPhoneAuthCredential(credential)
            }
            private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

            }
            override fun onVerificationFailed(e: FirebaseException) {

            }
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            )
            {
                verificationCodeSendBySystem = verificationId
                var resendToken = token
            }
        }
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                    .setPhoneNumber("+92$phoneNumber")
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(this)
                    .setCallbacks(callbacks)
                    .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        btnSignUpOfUser.setOnClickListener {
            var completeOTPEnterByUser: String = etOTPOfUser.text.toString()
            lateinit var auth: FirebaseAuth
            auth = Firebase.auth
            var userInformation: UserInformation = UserInformation()
            userInformation.intilizationAttribute(firstName, secondName, gender, passWord, userEmail, phoneNumber, Longitude, Latitude
            )
            val database = Firebase.database.reference
            //val myRef = database.getReference("User Information")
            auth.createUserWithEmailAndPassword(userEmail, passWord)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this, "User Create", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "${task.exception.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            database.child("Data").child("Employees").child(firstName+secondName).setValue(userInformation)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "The Data Is Stored", Toast.LENGTH_SHORT).show()
                        val shifter: Intent = Intent(this, LoginActivity::class.java)
                        startActivity(shifter)
                    } else {
                        Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}