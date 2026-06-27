package com.projectPAB.umkmkuningan.presentation.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.projectPAB.umkmkuningan.data.remote.RetrofitClient
import com.projectPAB.umkmkuningan.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (name.isEmpty()) {
                binding.tilName.error = "Nama tidak boleh kosong"
                return@setOnClickListener
            } else binding.tilName.error = null

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.error = "Email tidak valid"
                return@setOnClickListener
            } else binding.tilEmail.error = null

            if (phone.isEmpty()) {
                binding.tilPhone.error = "Nomor WhatsApp tidak boleh kosong"
                return@setOnClickListener
            } else binding.tilPhone.error = null

            if (password.length < 6) {
                binding.tilPassword.error = "Password minimal 6 karakter"
                return@setOnClickListener
            } else binding.tilPassword.error = null

            // Panggil API Register
            binding.btnRegister.isEnabled = false
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.backendApiService.register(name, email, phone, password)
                    if (response.isSuccessful && response.body()?.status == "success") {
                        Toast.makeText(this@RegisterActivity, "Pendaftaran berhasil! Silakan masuk.", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val errorMsg = response.body()?.message ?: "Gagal mendaftar"
                        Toast.makeText(this@RegisterActivity, errorMsg, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@RegisterActivity, "Gagal koneksi: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    binding.btnRegister.isEnabled = true
                }
            }
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }
}
