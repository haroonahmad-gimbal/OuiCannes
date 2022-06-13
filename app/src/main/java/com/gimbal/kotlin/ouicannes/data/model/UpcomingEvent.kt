package com.gimbal.kotlin.ouicannes.data.model

import android.util.Log
import com.google.firebase.firestore.QuerySnapshot

data class UpcomingEvent(
    val eventId : String,
    val title : String,
    val time: String,
    val url : String)


