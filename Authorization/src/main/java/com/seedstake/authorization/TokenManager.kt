package com.seedstake.authorization

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val accessExpiry: Long,
    val refreshExpiry: Long
) {
    fun isExpired(): Boolean {
        val currentTime = System.currentTimeMillis()
        return accessExpiry <= currentTime
    }
}

class TokenManager private constructor(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("TokenPreferences", Context.MODE_PRIVATE)
    private val gson: Gson = Gson()

    companion object {
        @Volatile
        private var instance: TokenManager? = null

        fun getInstance(context: Context): TokenManager {
            return instance ?: synchronized(this) {
                instance ?: TokenManager(context.applicationContext).also { instance = it }
            }
        }
    }

    fun getAuthToken(): AuthToken? {
        val authTokenJson = sharedPreferences.getString("authToken", null)
        return gson.fromJson(authTokenJson, AuthToken::class.java)
    }

    fun saveAuthToken(authToken: AuthToken) {
        sharedPreferences.edit {
            putString("authToken", gson.toJson(authToken))
        }
    }

    fun clearAuthToken() {
        sharedPreferences.edit {
            remove("authToken")
        }
    }

    fun isAuthTokenValid(): Boolean {
        val authToken = getAuthToken()
        return authToken?.isExpired() == false
    }

    private inline fun SharedPreferences.edit(commit: Boolean = false, action: SharedPreferences.Editor.() -> Unit) {
        val editor = edit()
        action(editor)
        if (commit) {
            editor.commit()
        } else {
            editor.apply()
        }
    }
}
