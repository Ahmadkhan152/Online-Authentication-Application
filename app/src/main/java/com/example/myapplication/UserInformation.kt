package com.example.myapplication

class UserInformation {

    lateinit var firstName:String;
    lateinit var secondName:String;
    lateinit var gender:String;
    lateinit var password: String
    lateinit var emailAddress:String
    lateinit var phoneNumber:String
    var Latitude:Double?=0.0
    var Longitude:Double?=0.0
    fun intilizationAttribute(firstNameOfUser:String, secondNameOfUser:String, genderOfUser:String, passwordOfUser:String, emailAddressOfUser:String, phoneNumberOfUser:String, LongitudeOfUser:Double, LatitudeOfUser:Double)
    {
        firstName=firstNameOfUser
        secondName=secondNameOfUser
        gender=genderOfUser
        password=passwordOfUser
        emailAddress=emailAddressOfUser
        phoneNumber=phoneNumberOfUser
        Latitude=LatitudeOfUser
        Longitude=LongitudeOfUser
    }
}