package com.dicoding.habitapp

import android.app.*
import androidx.appcompat.app.*
import androidx.preference.*
import com.dicoding.habitapp.setting.*
import com.dicoding.habitapp.utils.*
import org.koin.android.ext.koin.*
import org.koin.core.context.*
import java.util.*

class HabitApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        preferences.getString(
            getString(R.string.pref_key_dark),
            getString(R.string.pref_dark_follow_system)
        )?.apply {
            val mode = DarkMode.valueOf(this.uppercase(Locale.US))
            AppCompatDelegate.setDefaultNightMode(mode.value)
        }

        startKoin {
            androidContext(this@HabitApplication)
            modules(storageModule)
        }
    }
}