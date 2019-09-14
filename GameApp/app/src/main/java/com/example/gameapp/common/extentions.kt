package com.example.gameapp.common

import android.app.Activity
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*


//view group
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)

/* extension version */
fun <T> Iterable<T>.shuffle(): List<T> {
    val list = this.toMutableList().apply { }
    Collections.shuffle(list)
    return list
}

fun Activity.vibrate() {
    val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(200)
    }

}