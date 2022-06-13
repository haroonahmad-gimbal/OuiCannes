@file:JvmName("Utils")
@file:JvmMultifileClass

package com.gimbal.kotlin.ouicannes.utils

import android.content.Context
import android.widget.Toast
import java.util.concurrent.TimeUnit

fun showTokensEarnedDialog(context: Context, points: Int){
    Toast.makeText(context," Woo Hoo! You earned$points Points!",Toast.LENGTH_LONG).show()
//    val dialogBuilder = AlertDialog.Builder(context.applicationContext)
//    dialogBuilder.setMessage("You earned $points tokens!")
//        .setPositiveButton("OK",
//            DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
//            dialogInterface.dismiss()
//        })
//
//    val alert = dialogBuilder.create()
//    alert.setTitle("Woo Hoo!")
//    alert.show()
}

fun calculatePoints(dwellTimeinMillis : Long, basePoints : Int) : Int{
    val dwellTimeInSeconds =  TimeUnit.MILLISECONDS.toSeconds(dwellTimeinMillis)
    var points = 0

    points = when {
        dwellTimeInSeconds < 120 -> {
            basePoints
        }
        dwellTimeInSeconds in 120..239 -> {
            basePoints * 2
        }
        dwellTimeInSeconds in 240..479 -> {
            basePoints * 3
        }
        dwellTimeInSeconds in 480..959 -> {
            basePoints * 4
        }
        else -> {
            basePoints * 5
        }
    }
    return points
}