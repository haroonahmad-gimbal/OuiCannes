@file:JvmName("GimbalPlacesRepo")
@file:JvmMultifileClass
package com.gimbal.kotlin.ouicannes.utils

import android.content.Context
import android.content.SharedPreferences
import com.gimbal.kotlin.ouicannes.R
import com.gimbal.kotlin.ouicannes.data.model.GimbalEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.concurrent.TimeUnit

fun getGimbalVisits(context: Context) : List<GimbalEvent>?{
    var events: List<GimbalEvent>
    var gson = Gson()
    var type = object : TypeToken<List<GimbalEvent?>?>() {}.type
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )
    var serializedObject: String? = sharedPreferences.getString(context.getString(R.string.events_key), null)
    serializedObject?.let {
        events = gson.fromJson(serializedObject, type)
        return events
    } ?: run {
        return null
    }
}

fun saveGimbalEvents(context: Context, events : List<GimbalEvent>){
    var gson = Gson()
    val json: String = gson.toJson(events)
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )
    with (sharedPreferences?.edit()) {
        this?.putString(context?.getString(R.string.events_key), json)
        this?.apply()
    }
}

fun getPoints(context: Context) : Int {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )

    return sharedPreferences.getInt(context.getString(R.string.points_key), 0)
}

fun addPoints(context: Context, points : Int){
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )
    val totalPoints = getPoints(context) + points
    with (sharedPreferences.edit()) {
        this?.putInt(context.getString(R.string.points_key), totalPoints)
        this?.apply()
    }

    val db = Firebase.firestore
    var auth = Firebase.auth

    auth.currentUser?.let {
     val userPoints =  db.collection("users").document(it.uid)
        userPoints.update("points",totalPoints)
    }

}

fun calculateAndSetPoints(context: Context, dwellTimeinMillis : Long, basePoints : Int) : Int{

    val earnedPoints = calculatePoints(dwellTimeinMillis,basePoints)
    addPoints(context, earnedPoints )
    return earnedPoints
}

