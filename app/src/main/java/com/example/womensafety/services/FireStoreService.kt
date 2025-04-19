package com.example.womensafety.services

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface FireStoreService {
    suspend fun addDocument(collection: String, data: Map<String, Any>): String
    suspend fun addDocument(collection: String, documentId: String, data: Map<String, Any>)
    suspend fun getDocument(collection: String, documentId: String): DocumentSnapshot?
    suspend fun updateDocument(collection: String, documentId: String, data: Map<String, Any>)
    suspend fun deleteDocument(collection: String, documentId: String)
    suspend fun queryDocuments(collection: String, field: String, value: Any): QuerySnapshot
    fun listenToDocument(collection: String, documentId: String): Flow<DocumentSnapshot>
    fun listenToCollection(collection: String): Flow<QuerySnapshot>
}