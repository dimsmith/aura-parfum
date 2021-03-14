package com.github.dimsmith.auraparfum.components

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    private val _loader = MutableLiveData(false)
    val loader: LiveData<Boolean>
        get() = _loader
    private val _exception = MutableLiveData<Exception>()
    val exception: LiveData<Exception>
        get() = _exception

    protected fun postLoader(value: Boolean) {
        this._loader.postValue(value)
    }

    protected fun postException(ex: Exception) {
        this._exception.postValue(ex)
    }
}