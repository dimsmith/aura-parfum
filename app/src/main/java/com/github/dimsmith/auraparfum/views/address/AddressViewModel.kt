package com.github.dimsmith.auraparfum.views.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.github.dimsmith.auraparfum.common.MyResult
import com.github.dimsmith.auraparfum.components.BaseViewModel
import com.github.dimsmith.auraparfum.data.model.Address
import com.github.dimsmith.auraparfum.data.repositories.AddressRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddressViewModel : BaseViewModel() {
    private val repo = AddressRepository()
    private val auth = FirebaseAuth.getInstance()
    private val _onCompleted = MutableLiveData<String>()
    val onCompleted : LiveData<String> = _onCompleted

    @ExperimentalCoroutinesApi
    val fetchAddress = liveData<MyResult<List<Address>>>(Dispatchers.IO) {
        auth.currentUser?.let { firebaseUser ->
            val uid = firebaseUser.uid
            try {
                repo.fetch(uid)
                    .collect {
                        when (it) {
                            is MyResult.Failure -> {
                                throw it.ex
                            }
                            is MyResult.Success -> emit(it)
                        }
                    }
            } catch (e: Exception) {
                emit(MyResult.Failure(e))
            }
        }
    }

    fun addAddress(address: Address) {
        CoroutineScope(Dispatchers.IO).launch {
            auth.currentUser?.let { firebaseUser ->
                try {
                    postLoader(true)
                    repo.add(firebaseUser.uid, address)
                    _onCompleted.postValue("Saved!")
                } catch (e: Exception) {
                    postException(e)
                } finally {
                    postLoader(false)
                }
            }
        }
    }
}