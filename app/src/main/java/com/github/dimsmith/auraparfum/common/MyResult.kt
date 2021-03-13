package com.github.dimsmith.auraparfum.common

sealed class MyResult<out T> {
    class Success<out T>(val data: T) : MyResult<T>()
    class Failure<out E>(val ex: Exception) : MyResult<E>()
}