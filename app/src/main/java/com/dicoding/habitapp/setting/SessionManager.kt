package com.dicoding.habitapp.setting

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    fun register(username: String, password: String): Boolean  {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_PASSWORD, password)
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.apply()
        return editor.commit()
    }

    fun login(username: String, password: String): Boolean {
        val savedUsername = sharedPreferences.getString(KEY_USERNAME, "")
        val savedPassword = sharedPreferences.getString(KEY_PASSWORD, "")

        val isLoggedIn = username == savedUsername && password == savedPassword
        if (isLoggedIn) {
            setLoggedIn(true)
        }
        return isLoggedIn
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun getFromPreference(key: String): String? = sharedPreferences.getString(key, "")


    fun logout() {
        val editor = sharedPreferences.edit()
        editor.remove(KEY_USERNAME)
        editor.remove(KEY_PASSWORD)
        editor.remove(KEY_IS_LOGGED_IN)
        editor.apply()
    }
}
