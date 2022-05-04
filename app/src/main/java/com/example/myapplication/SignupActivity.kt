package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {
    lateinit var fusedLocationProviderClient:FusedLocationProviderClient
    lateinit var tvLocationOfUser:TextView
    var latitude:Double=0.0;
    var longitude:Double=0.0;
    fun checkValidatePassword(pW:String): Boolean {
        val PASSWORD_PATTERN:String = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
        var pattern = Pattern.compile(PASSWORD_PATTERN);
        var matcher = pattern.matcher(pW);

        return matcher.matches();
    }
    fun checkValidateEmail(emailID:String):Boolean{

        val PATTERN_EMAIL:String="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        var pattern:Pattern= Pattern.compile(PATTERN_EMAIL)
        var matcher = pattern.matcher(emailID)
        return matcher.matches();
    }
    fun checkValidateMobileNumber(number:String):Boolean{
        val PATTERN_NO:String="[3][0-9]"
        var pattern:Pattern= Pattern.compile(PATTERN_NO)
        var matcher=pattern.matcher(number)
        return matcher.matches()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        getSupportActionBar()?.setTitle("SIGN UP")
        val etFirstNameOfUser:EditText=findViewById(R.id.etFirstNameOfUser)
        val etSecondNameOfUser:EditText=findViewById(R.id.etSecondNameOfUser)
        val radioMaleBtn:RadioButton=findViewById(R.id.radioMaleButton)
        val radioFemaleBtn:RadioButton=findViewById(R.id.radioFemaleButton)
        val etPasswordOfUser:EditText=findViewById(R.id.etPasswordOfUser)
        val etEmailAddressOfUser:EditText=findViewById(R.id.etEmailAddressOfUser)
        val etPhoneNumberOfUser:EditText=findViewById(R.id.etPhoneNumberOfUser)
        val radioGroup:RadioGroup=findViewById(R.id.GenderGroup)
        val btnSignUp:TextView=findViewById(R.id.btnSignUp)
        val layoutPasswordOfUser:TextInputLayout=findViewById(R.id.layoutPasswordOfUser)
        val layoutEmailAddressOfUser:TextInputLayout=findViewById(R.id.layoutEmailAddressOfUser)
        val layoutPhoneNumberOfUser:TextInputLayout=findViewById(R.id.layoutPhoneNumber)
        tvLocationOfUser=findViewById(R.id.tvLocationOfUser)
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
        btnSignUp.setOnClickListener({

            if (etFirstNameOfUser.text.toString().isEmpty())
            {
                etFirstNameOfUser.requestFocus()
                etFirstNameOfUser.setError("Error! Empty Field Not Allowed")
            }
            else if (etSecondNameOfUser.text.toString().isEmpty())
            {
                etSecondNameOfUser.requestFocus()
                etSecondNameOfUser.setError("Error! Empty Field Not Allowed")
            }
            else if (radioGroup.checkedRadioButtonId == -1)
            {
                Toast.makeText(this,"Please Select The Gender",Toast.LENGTH_SHORT).show()
            }
            else if (etPasswordOfUser.text.toString().isEmpty())
            {
                layoutPasswordOfUser.requestFocus()
                layoutPasswordOfUser.setError("Error! Empty Field Not Allowed")
            }
            else if (etEmailAddressOfUser.text.toString().isEmpty())
            {
                etEmailAddressOfUser.requestFocus()
                etEmailAddressOfUser.setError("Error! Empty Field Not Allowed")
            }
            else if (etPhoneNumberOfUser.text.toString().isEmpty())
            {
                etPhoneNumberOfUser.requestFocus()
                etPhoneNumberOfUser.setError("Error! Empty Field Not Allowed")
            }
            else
            {
                var firstName:String=etFirstNameOfUser.text.toString()
                var secondName:String=etSecondNameOfUser.text.toString()
                var password:String=etPasswordOfUser.text.toString()
                var emailAddress:String=etEmailAddressOfUser.text.toString()
                var phoneNumber:String=etPhoneNumberOfUser.text.toString()
                var gender:String="Male"
                if (radioFemaleBtn.isChecked())
                    gender="Female"
                if (!checkValidatePassword(password))
                {
                    layoutPasswordOfUser.requestFocus()
                    layoutPasswordOfUser.setError("Error! Invalid Password")
                }
                else if(!checkValidateEmail(emailAddress))
                {
                    layoutEmailAddressOfUser.requestFocus()
                    layoutEmailAddressOfUser.setError("Error! Invalid Email Address")
                }
                /*else if(!checkValidateMobileNumber(number))
                {
                    inputLayout3.requestFocus()
                    inputLayout3.setError("Error! Invalid Email Address")
                }*/
                else
                {
                    //Toast.makeText(this,"$pW",Toast.LENGTH_SHORT).show();
                    val shifter:Intent=Intent(this,OTPActivity::class.java)
                    shifter.putExtra("first_Name",firstName)
                    shifter.putExtra("second_Name",secondName)
                    shifter.putExtra("Gender",gender)
                    shifter.putExtra("PassWord",password)
                    shifter.putExtra("User_Email",emailAddress)
                    shifter.putExtra("Number",phoneNumber)
                    shifter.putExtra("LongitudeLocation",longitude)
                    shifter.putExtra("LatitudeLocation",latitude)
                    startActivity(shifter)
                }

            }
        })
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            getLocation();
        }
        else{
            askLocationPermission()
        }
    }
    private fun askLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),568)
            }
            else
            {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),568)
            }
        }
    }
    private fun getLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 569)
        }
        var locationTask:Task<Location> =fusedLocationProviderClient.lastLocation
        locationTask.addOnSuccessListener{ location : Location? ->
            if (location!=null)
            {
                tvLocationOfUser.setText("Your Location is: \nLatitude: ${location.latitude} \nLongitude: ${location.longitude}")
                latitude=location.latitude
                longitude=location.longitude
            }
        }
        locationTask.addOnFailureListener{
            Toast.makeText(this,"Location Failed",Toast.LENGTH_SHORT).show();
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            getLocation();
        }
        else
            Toast.makeText(this, "Permission denied by user", Toast.LENGTH_SHORT).show()

    }
}