package com.github.dimsmith.auraparfum.data.repositories

import androidx.lifecycle.LiveData
import com.github.dimsmith.auraparfum.common.MyResult
import com.github.dimsmith.auraparfum.common.await
import com.github.dimsmith.auraparfum.common.awaitTaskCompletable
import com.github.dimsmith.auraparfum.data.FirebaseHelper
import com.github.dimsmith.auraparfum.data.model.Address
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

class AddressRepository {
    private val db = FirebaseFirestore.getInstance()

    companion object {
        const val TAG = "AddressRepository"
        const val COLLECTION_PATH = "addresses"
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
    suspend fun fetch(uid: String): Flow<MyResult<List<Address>>> {
        return callbackFlow {
            val subscription = db.document(FirebaseHelper.pathUser(uid)).collection(COLLECTION_PATH)
                .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, error ->
                    if (error != null) {
                        offer(MyResult.Failure(error))
                    }
                    querySnapshot?.let {
                        offer(MyResult.Success(it.toObjects(Address::class.java)))
                    }
                }
            awaitClose {
                subscription.remove()
            }
        }
    }

    suspend fun add(uid: String, address: Address): MyResult<Address> {
        return try {
            db.document(FirebaseHelper.pathUser(uid)).collection(COLLECTION_PATH)
                .add(address)
                .awaitTaskCompletable()
            MyResult.Success(address)
        } catch (e: Exception) {
            MyResult.Failure(e)
        }
    }

//    suspend fun retrieve(uid: String): MyResult<Address> {
//        return request("${FirebaseHelper.pathUser(uid)}/addresses")
//    }

//    private suspend fun request(path: String): MyResult<Address> {
//        return try {
//            val docRef = db.document(path)
//            val result = docRef.get().await()
//            MyResult.Success(toAddress(result))
//        } catch (e: Exception) {
//            Timber.e(e, TAG)
//            MyResult.Failure(e)
//        }
//    }

}