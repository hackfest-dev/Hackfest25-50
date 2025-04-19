package com.example.womensafety.services.impl

import com.example.womensafety.services.FireStoreService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FireStoreServiceImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : FireStoreService {

    override suspend fun addDocument(collection: String, data: Map<String, Any>): String {
        check(auth.currentUser != null) { "User must be authenticated" }
        if (collection == "families") {
            // Ensure guardianEmail or childEmail matches the user's UID
            val uid = auth.currentUser!!.uid
            check(data["guardianEmail"] == uid || data["childEmail"] == uid) {
                "Data must include guardianEmail or childEmail matching the user's UID"
            }
        }
        val documentRef = firestore.collection(collection).add(data).await()
        return documentRef.id
    }

    override suspend fun addDocument(collection: String, documentId: String, data: Map<String, Any>) {
        check(auth.currentUser != null) { "User must be authenticated" }
        if (collection == "families") {
            val uid = auth.currentUser!!.uid
            check(data["guardianEmail"] == uid || data["childEmail"] == uid) {
                "Data must include guardianEmail or childEmail matching the user's UID"
            }
        }
        firestore.collection(collection).document(documentId).set(data).await()
    }

    override suspend fun getDocument(collection: String, documentId: String): DocumentSnapshot? {
        check(auth.currentUser != null) { "User must be authenticated" }
        return firestore.collection(collection).document(documentId).get().await()
    }

    override suspend fun updateDocument(collection: String, documentId: String, data: Map<String, Any>) {
        check(auth.currentUser != null) { "User must be authenticated" }
        firestore.collection(collection).document(documentId).update(data).await()
    }

    override suspend fun deleteDocument(collection: String, documentId: String) {
        check(auth.currentUser != null) { "User must be authenticated" }
        firestore.collection(collection).document(documentId).delete().await()
    }

    override suspend fun queryDocuments(collection: String, field: String, value: Any): QuerySnapshot {
        check(auth.currentUser != null) { "User must be authenticated" }
        return firestore.collection(collection).whereEqualTo(field, value).get().await()
    }

    override fun listenToDocument(collection: String, documentId: String): Flow<DocumentSnapshot> {
        return callbackFlow {
            check(auth.currentUser != null) { "User must be authenticated" }
            val listenerRegistration = firestore.collection(collection).document(documentId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    snapshot?.let { trySend(it) }
                }
            awaitClose { listenerRegistration.remove() }
        }
    }

    override fun listenToCollection(collection: String): Flow<QuerySnapshot> {
        return callbackFlow {
            check(auth.currentUser != null) { "User must be authenticated" }
            val listenerRegistration = firestore.collection(collection)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    snapshot?.let { trySend(it) }
                }
            awaitClose { listenerRegistration.remove() }
        }
    }
}