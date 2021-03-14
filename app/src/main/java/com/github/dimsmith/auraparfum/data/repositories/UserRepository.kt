package com.github.dimsmith.auraparfum.data.repositories

import com.github.dimsmith.auraparfum.common.MyResult
import com.github.dimsmith.auraparfum.common.await
import com.github.dimsmith.auraparfum.data.FirebaseHelper
import com.github.dimsmith.auraparfum.data.model.User
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.firestoreSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

class UserRepository {
    private val db = FirebaseFirestore.getInstance()

    companion object {
        const val TAG = "UserRepository"
    }

    init {
        /**
         * Available for offline mode
         */
        db.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = true
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun getUser(uid: String): Flow<MyResult<User>> {
        return callbackFlow {
            val subscription = db.document(FirebaseHelper.pathUser(uid))
                .addSnapshotListener(MetadataChanges.INCLUDE) { docSnapshot, error ->
                    if (error != null) {
                        offer(MyResult.Failure(Exception("TEST")))
                    }
                    if (docSnapshot == null) {
                        val user = toUser(docSnapshot)
                        offer(MyResult.Success(user))
                    }
                }
            awaitClose {
                subscription.remove()
            }
        }
    }

    suspend fun retrieve(uid: String): MyResult<User> {
        return request(FirebaseHelper.pathUser(uid))
    }

    private suspend fun request(path: String): MyResult<User> {
        return try {
            val docRef = db.document(path)
            val result = docRef.get().await()
            MyResult.Success(toUser(result))
        } catch (e: Exception) {
            Timber.e(e, TAG)
            MyResult.Failure(e)
        }
    }

    private fun toUser(snapshot: DocumentSnapshot?) : User {
        if (snapshot == null) throw Exception("User not found")
        return User(
            snapshot.id,
            snapshot["firstName"].toString(),
            snapshot["lastName"].toString(),
            snapshot["email"].toString()
        )
    }
}