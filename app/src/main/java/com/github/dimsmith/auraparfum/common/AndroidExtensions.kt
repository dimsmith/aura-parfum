package com.github.dimsmith.auraparfum.common

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

const val TAG = "AndroidExtension.kt"
internal suspend fun <T> Task<T>.await(): T? {
    if (isComplete) {
        val ex = exception
        return if (ex == null) {
            if (isCanceled) {
                Timber.d(exception, "$TAG: Task canceled by user.")
                throw CancellationException("Task canceled by user")
            } else result
        } else throw ex
    }
    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            val ex = exception
            if (ex == null) {
                if (isCanceled) cont.cancel()
                else cont.resume(result)
            } else cont.resumeWithException(ex)
        }
    }
}