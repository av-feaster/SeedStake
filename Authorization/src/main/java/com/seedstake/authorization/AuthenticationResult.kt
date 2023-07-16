package com.seedstake.authorization


public data class User(
    val id: String,
    val username: String,
    val passwordHash: String,
    val email: String,
    var mfaEnabled: Boolean = false,
    var mfaSecretKey: String? = null
)


public class AuthenticationResult private constructor(
    val isSuccess: Boolean,
    val user: User?,  // Update the type to User?
    val errorMessage: String?,  // Update the type to String?
    val sessionToken: String?
) {
    companion object {
        fun success(user: User?, sessionToken: String?): AuthenticationResult {
            return AuthenticationResult(true, user, null, sessionToken)
        }

        fun failure(errorMessage: String?): AuthenticationResult {
            return AuthenticationResult(false, null, errorMessage, null)
        }
    }
}
