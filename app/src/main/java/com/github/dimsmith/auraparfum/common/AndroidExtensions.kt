package com.github.dimsmith.auraparfum.common

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.os.bundleOf
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
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

internal fun Context.makeToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

internal fun Context.makeSnackBar(view: View, msg: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, view, msg, duration).show()
}

internal fun Context.showAlertDialog(message: String, listener: DialogInterface.OnClickListener) {
    val alert = MaterialAlertDialogBuilder(this)
        .setTitle(message)
        .setPositiveButton("OK", listener)
        .setNegativeButton("Cancel") { dialog, whic ->
            dialog.cancel()
        }
    alert.show()
}

internal fun Activity.hideKeyboard() {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view: View? = this.currentFocus
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

internal fun <A : Activity> Context.toActivity(
    activity: Class<A>,
    bundle: Bundle = bundleOf(),
    clearTask: Boolean = false
) {
    val intent = Intent(this, activity)
    intent.putExtras(bundle)
    if (clearTask) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}