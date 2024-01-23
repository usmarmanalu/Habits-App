package com.dicoding.habitapp.ui.signup

import android.content.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.*
import com.dicoding.habitapp.*
import com.dicoding.habitapp.data.repository.*
import com.dicoding.habitapp.databinding.*
import com.dicoding.habitapp.setting.*
import com.dicoding.habitapp.ui.login.*

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Register"

        userRepository = UserRepository.getInstance(SessionManager(this))

        binding.registerButton.setOnClickListener {
            val username = binding.edUsername.text.toString()
            val password = binding.edPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.username_dan_password_tidak_boleh_kosong), Toast.LENGTH_SHORT).show()
            } else {
                isLoading(true)

                if (userRepository.registerUser(username, password)) {
                    Toast.makeText(this, getString(R.string.registrasi_berhasil), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this,
                        getString(R.string.gagal_melakukan_registrasi), Toast.LENGTH_SHORT).show()
                }

                isLoading(false)
            }
        }
    }

    private fun isLoading(isLoading: Boolean) {
        val progressBar = binding.progressBarSignup
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}



