package com.github.dimsmith.auraparfum.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.dimsmith.auraparfum.common.BaseViewModel
import com.github.dimsmith.auraparfum.common.MyResult
import com.github.dimsmith.auraparfum.data.model.User
import com.github.dimsmith.auraparfum.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel() {
    private val repo = UserRepository()

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun fetchUser() {
        val uid = "uJAm75clhahIaGW4MujKhTI87B93"
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repo.retrieve(uid)) {
                is MyResult.Success -> _user.postValue(result.data)
                is MyResult.Failure -> postException(result.ex)
            }
        }

    }

}