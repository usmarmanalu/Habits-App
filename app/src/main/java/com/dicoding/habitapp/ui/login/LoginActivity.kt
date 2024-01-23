package com.dicoding.habitapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.habitapp.*
import com.dicoding.habitapp.data.repository.UserRepository
import com.dicoding.habitapp.databinding.*
import com.dicoding.habitapp.ui.list.HabitListActivity
import com.dicoding.habitapp.ui.signup.*
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val userRepository: UserRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Login"

        if (userRepository.isUserLoggedIn()) {
            moveToHomeActivity()
        }

        binding.btnLogin.setOnClickListener {
            saveSession()
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveSession() {
        val username = binding.edUsername.text.toString()
        val password = binding.edPassword.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,
                getString(R.string.username_dan_password_tidak_boleh_kosong), Toast.LENGTH_SHORT).show()
        } else {
            isLoading(true)
            if (userRepository.loginUser(username, password)) {
                moveToHomeActivity()
                Toast.makeText(this, getString(R.string.berhasil_login), Toast.LENGTH_SHORT).show()

            } else {
                isLoading(false)
                Toast.makeText(this, getString(R.string.akun_tidak_terdaftar), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun moveToHomeActivity() {
        startActivity(Intent(this, HabitListActivity::class.java))
        finish()
    }

    private fun isLoading(isLoading: Boolean) {
        val progressBar = binding.progressBarSigin
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

