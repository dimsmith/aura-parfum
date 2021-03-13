package com.github.dimsmith.auraparfum.data

object FirebaseHelper {
    private const val BASE_PATH_FIRESTORE = "/v2/auraparfum-v2-data"

    fun pathUser(uid: String) = "$BASE_PATH_FIRESTORE/users/$uid"
}