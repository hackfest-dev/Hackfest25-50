package com.example.womensafety.services


interface AccountService {
    suspend fun signIn(email: String, password: String)
    suspend fun signUp(email: String, password: String)
    fun signOut()
    fun getCurrentUserEmail(): String?
}
