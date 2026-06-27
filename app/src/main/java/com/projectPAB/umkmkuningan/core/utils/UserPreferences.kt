package com.projectPAB.umkmkuningan.core.utils

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("umkm_prefs", Context.MODE_PRIVATE)

    fun saveUser(id: Int, name: String, email: String, phone: String) {
        prefs.edit().apply {
            putInt("USER_ID", id)
            putString("USER_NAME", name)
            putString("USER_EMAIL", email)
            putString("USER_PHONE", phone)
            putBoolean("IS_LOGGED_IN", true)
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("IS_LOGGED_IN", false)
    }

    fun getUserId(): Int {
        return prefs.getInt("USER_ID", 0)
    }

    fun getUserName(): String {
        return prefs.getString("USER_NAME", "Pengguna") ?: "Pengguna"
    }

    fun getUserEmail(): String {
        return prefs.getString("USER_EMAIL", "") ?: ""
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}
