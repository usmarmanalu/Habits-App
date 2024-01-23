package com.dicoding.habitapp.data.repository

import com.dicoding.habitapp.setting.*

class UserRepository(private val sessionManager: SessionManager) {

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(sessionManager: SessionManager): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(sessionManager)
            }
    }

    fun loginUser(username: String, password: String): Boolean {
        if (sessionManager.login(username, password)) {
            sessionManager.setLoggedIn(true)
            return true
        }
        return false
    }

    fun registerUser(username: String, password: String): Boolean {
        if (!sessionManager.isLoggedIn()) {
            sessionManager.register(username, password)
            return true
        }
        return false
    }

    fun getUser(): String? = sessionManager.getFromPreference(SessionManager.KEY_USERNAME)

    fun isUserLoggedIn(): Boolean = sessionManager.isLoggedIn()

    fun logoutUser() = sessionManager.logout()
}
