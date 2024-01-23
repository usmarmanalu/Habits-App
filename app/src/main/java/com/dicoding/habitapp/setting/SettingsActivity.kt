package com.dicoding.habitapp.setting

import android.Manifest
import android.os.*
import android.widget.*
import androidx.activity.result.contract.*
import androidx.appcompat.app.*
import androidx.preference.*
import com.dicoding.habitapp.R
import com.dicoding.habitapp.utils.*

class SettingsActivity : AppCompatActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Notifications permission granted")
            } else {
                showToast("Notifications will not show without permission")
            }
        }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT > 32) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            // Update theme based on value in ListPreference
            val switchDarkMode: ListPreference? = findPreference(getString(R.string.pref_key_dark))
            switchDarkMode?.setOnPreferenceChangeListener { _, newValue ->
                val stringValue = newValue.toString()
                if (stringValue == getString(R.string.pref_dark_follow_system)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        updateTheme(DarkMode.FOLLOW_SYSTEM.value)
                    } else {
                        updateTheme(DarkMode.ON.value)
                    }
                } else if (stringValue == getString(R.string.pref_dark_off)) {
                    updateTheme(DarkMode.OFF.value)
                } else {
                    updateTheme(DarkMode.ON.value)
                }
                true
            }
        }

        private fun updateTheme(mode: Int): Boolean {
            AppCompatDelegate.setDefaultNightMode(mode)
            requireActivity().recreate()
            return true
        }
    }
}