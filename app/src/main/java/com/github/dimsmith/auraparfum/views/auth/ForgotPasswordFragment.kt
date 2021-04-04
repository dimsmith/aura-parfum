package com.github.dimsmith.auraparfum.views.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.dimsmith.auraparfum.R
import com.github.dimsmith.auraparfum.common.*
import com.github.dimsmith.auraparfum.common.Constanta.DEFAULT_ERROR_MESSAGE
import com.github.dimsmith.auraparfum.common.awaitTaskCompletable
import com.github.dimsmith.auraparfum.common.hideKeyboard
import com.github.dimsmith.auraparfum.common.initProgressBar
import com.github.dimsmith.auraparfum.common.makeToast
import com.github.dimsmith.auraparfum.components.BaseViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForgotPasswordFragment : Fragment() {
    private lateinit var emailLayout: TextInputLayout
    private lateinit var emailInput: EditText
    private lateinit var resetBtn: Button
    private lateinit var loader: AlertDialog
    private lateinit var vm: ForgotPasswordViewModel

    internal class ForgotPasswordViewModel : BaseViewModel() {
        private val auth = FirebaseAuth.getInstance()
        private val _onCompleted = MutableLiveData<String>()
        val onCompleted: LiveData<String> = _onCompleted

        @SuppressLint("LogNotTimber")
        fun resetPassword(email: String) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    postLoader(true)
                    auth.sendPasswordResetEmail(email).awaitTaskCompletable()
                    _onCompleted.postValue("Please check your email.")
                } catch (e: Exception) {
                    var msg = "Failed to reset password."
                    when (e) {
                        is FirebaseAuthInvalidCredentialsException -> msg =
                            "The email address is badly formatted."
                        is FirebaseAuthInvalidUserException -> msg =
                            "There is no user record corresponding to this identifier."
                    }
                    postException(AuthException(msg))
                    Log.e("resetPassword", e.message ?: "")
                } finally {
                    postLoader(false)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerElement(view)
    }

    private fun registerElement(view: View) {
        emailLayout = view.findViewById(R.id.email_txt_layout)
        emailInput = emailLayout.editText!!
        resetBtn = view.findViewById(R.id.reset_btn)
        loader = requireContext().initProgressBar(resources)
        resetBtn.setOnClickListener { doReset() }
        vm = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)
    }

    @SuppressLint("LogNotTimber")
    private fun doReset() {
        requireActivity().hideKeyboard()
        val email = emailInput.text.toString()
        if (email.isEmpty()) {
            emailLayout.error = "Email is required"
            return
        }
        emailLayout.error = ""
        vm.resetPassword(email)
        vm.onCompleted.observe(viewLifecycleOwner) {
            requireActivity().makeToast(it)
            findNavController().navigateUp()
        }
        vm.loader.observe(viewLifecycleOwner) {
            if (it) loader.show()
            else loader.dismiss()
        }
        vm.exception.observe(viewLifecycleOwner) {
            requireActivity().makeToast(it.message ?: DEFAULT_ERROR_MESSAGE)
        }
    }

}