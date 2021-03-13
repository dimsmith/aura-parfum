package com.github.dimsmith.auraparfum.data.repositories

import com.github.dimsmith.auraparfum.common.MyResult
import com.github.dimsmith.auraparfum.common.await
import com.github.dimsmith.auraparfum.data.FirebaseHelper
import com.github.dimsmith.auraparfum.data.model.User
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    companion object {
        const val TAG = "UserRepository"
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
            snapshot["lastName"].toString()
        )
    }
}