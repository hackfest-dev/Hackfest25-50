package com.example.womensafety.services.impl

import com.example.womensafety.services.AccountService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AccountServiceImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : AccountService {

    override suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }
}
