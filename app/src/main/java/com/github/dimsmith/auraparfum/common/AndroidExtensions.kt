package com.github.dimsmith.auraparfum.common

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import com.github.dimsmith.auraparfum.R
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.text.NumberFormat
import java.util.*
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

internal suspend fun <T> Task<T>.awaitTaskCompletable() {
    return suspendCoroutine { continuation ->
        this.addOnCompleteListener { task ->
            if (task.isSuccessful)
                continuation.resume(Unit)
            else {
                this.exception?.let {
                    continuation.resumeWithException(it)
                }
            }
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
        .setNegativeButton("Cancel") { dialog, _ ->
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

internal fun formatCurrency(value: Double, withLocale: Boolean = false): String {
    val nf = if (withLocale) {
        val locale = Locale("in", "ID")
        NumberFormat.getCurrencyInstance(locale)
    } else {
        NumberFormat.getNumberInstance()
    }
    return nf.format(value).toString()
}

internal fun Context.initProgressBar(resources: Resources): AlertDialog {
    val ctx = this
    val view = LayoutInflater.from(this).inflate(R.layout.dialog_progress_bar, null, false)
    return MaterialAlertDialogBuilder(this).apply {
        setView(view)
        background = ResourcesCompat.getDrawable(resources, R.color.white, ctx.theme)
        setCancelable(false)
    }.create()
}