package com.dicoding.habitapp.utils

import android.os.*
import androidx.annotation.*
import java.time.*
import java.time.format.*
import java.util.*


object DateFormatter {
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(currentDateString: String, targetTimeZone: String): String {
        val instant = Instant.parse(currentDateString)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy | HH:mm")
            .withZone(ZoneId.of(targetTimeZone))
        return formatter.format(instant)
    }
}
