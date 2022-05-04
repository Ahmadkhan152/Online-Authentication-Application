package com.example.myapplication

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class GooglemapActivity : AppCompatActivity()
{
    lateinit var sharedPreference:SharedPreferences;
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var  mapFragment:SupportMapFragment;
    var latitude:Double=31.4469;
    var longitude:Double=74.2682;
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_googlemap)
        sharedPreference=getSharedPreferences("UserCheck", MODE_PRIVATE)
        val sharedUserName: String? =sharedPreference.getString("User_Name","Undefine")
        var tvFirstNameOfUser:TextView=findViewById(R.id.tvFirstNameOfUser)
        var tvSecondNameOfUser:TextView=findViewById(R.id.tvSecondNameOfUser)
        var tvUserNameOfUser:TextView=findViewById(R.id.tvUserNameOfUser)
        var tvPasswordOfUser:TextView=findViewById(R.id.tvPasswordOfUser)
        var tvGenderOfUser:TextView=findViewById(R.id.tvGenderOfUser)
        var tvPhoneNumberOfUser:TextView=findViewById(R.id.tvPhoneNumberOfUser)
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        getSupportActionBar()?.setTitle("Google Map")
        mapFragment= supportFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment
        getCurrentLocation()
        var database=Firebase.database
        val data=database.getReference()
        data.child("Data").child("Employees").child("AhmadKhan").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot)
            {
                val temp = snapshot.getValue<UserInformation>()
                tvUserNameOfUser.setText("Your User Name: "+temp?.emailAddress)
                tvPasswordOfUser.setText("Your Password Is: "+temp?.password)
                tvFirstNameOfUser.setText("Your First Name Is: "+temp?.firstName)
                tvSecondNameOfUser.setText("Your Second Name Is: "+temp?.secondName)
                tvGenderOfUser.setText("Your Gender: "+temp?.gender)
                tvPhoneNumberOfUser.setText("Your Phone Number: "+temp?.phoneNumber)
            }
            override fun onCancelled(error: DatabaseError)
            {
                // Toast.makeText(this,"Operation Failed!!",Toast.LENGTH_SHORT).show()
            }

        })
    //Toast.makeText(this,""+temp,Toast.LENGTH_SHORT).show()
        /*data.child(temp.toString()).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot)
            {
                val temp = snapshot.getValue<UserInformation>()
                user_name.setText(temp?.e_Mail)
                password.setText(temp?.password)
                first_name.setText(temp?.first_Name)
                second_name.setText(temp?.second_Name)
                gender.setText(temp?.gender)
                phoneNo.setText(temp?.phone_Number)
            }
            override fun onCancelled(error: DatabaseError)
            {
               // Toast.makeText(this,"Operation Failed!!",Toast.LENGTH_SHORT).show()
            }

        })*/
    }
    private fun getCurrentLocation()
    {
        mapFragment.getMapAsync { google ->
            val pos = LatLng(latitude, longitude)
            val markerOptions:MarkerOptions=MarkerOptions().position(pos).title("You are here...")
            google.addMarker(markerOptions)
            google.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 20F))
        }
    }
    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            getLocation();
        }
        else{
            askLocationPermission()
        }
    }
    private fun askLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
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
        var locationTask: Task<Location> =fusedLocationProviderClient.lastLocation
        locationTask.addOnSuccessListener { location: Location? ->
            if (location != null)
            {
                latitude=location.latitude
                longitude=location.longitude
            }
        }
        locationTask.addOnFailureListener{
            Toast.makeText(this,"Location Failed", Toast.LENGTH_SHORT).show();
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
