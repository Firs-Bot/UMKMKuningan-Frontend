package com.projectPAB.umkmkuningan.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.projectPAB.umkmkuningan.MainActivity
import com.projectPAB.umkmkuningan.core.utils.UserPreferences
import com.projectPAB.umkmkuningan.data.remote.RetrofitClient
import com.projectPAB.umkmkuningan.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userPrefs: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPrefs = UserPreferences(this)

        if (userPrefs.isLoggedIn()) {
            navigateToMain()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty()) {
                binding.tilEmail.error = "Email tidak boleh kosong"
                return@setOnClickListener
            } else {
                binding.tilEmail.error = null
            }

            if (password.isEmpty()) {
                binding.tilPassword.error = "Password tidak boleh kosong"
                return@setOnClickListener
            } else {
                binding.tilPassword.error = null
            }

            // Memanggil API Login
            binding.btnLogin.isEnabled = false
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.backendApiService.login(email, password)
                    if (response.isSuccessful && response.body()?.status == "success") {
                        val user = response.body()?.user
                        if (user != null) {
                            userPrefs.saveUser(user.id, user.name, user.email, user.phone)
                            Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
                            navigateToMain()
                        }
                    } else {
                        val errorMsg = response.body()?.message ?: "Email atau password salah"
                        Toast.makeText(this@LoginActivity, errorMsg, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, "Gagal koneksi: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    binding.btnLogin.isEnabled = true
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
