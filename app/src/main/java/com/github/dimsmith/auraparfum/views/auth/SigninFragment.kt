package com.github.dimsmith.auraparfum.views.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.dimsmith.auraparfum.R
import com.github.dimsmith.auraparfum.common.*
import com.github.dimsmith.auraparfum.views.main.MainActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SigninFragment : Fragment() {
    private lateinit var signInBtn: Button
    private lateinit var signUpBtn: Button
    private lateinit var resetTxt: TextView
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loader: AlertDialog
    private val auth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerElement(view)
    }

    private fun registerElement(view: View) {
        signInBtn = view.findViewById(R.id.sign_in_btn)
        signUpBtn = view.findViewById(R.id.sign_up_btn)
        resetTxt = view.findViewById(R.id.reset_txt)
        emailLayout = view.findViewById(R.id.email_txt_layout)
        passwordLayout = view.findViewById(R.id.password_txt_layout)
        loader = requireContext().initProgressBar(resources)
        emailInput = emailLayout.editText!!
        passwordInput = passwordLayout.editText!!

        signInBtn.setOnClickListener { doLogin() }
        signUpBtn.setOnClickListener { doRegister() }
        resetTxt.setOnClickListener { doResetPassword() }
    }

    private fun doLogin() {
        val email = emailInput.text
        val password = passwordInput.text
        if (validate(email.toString(), password.toString())) {
            loginFirebase(email.toString(), password.toString())
        }
    }

    private fun validate(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            emailLayout.error = "Email is required"
            return false
        }
        emailLayout.error = ""

        if (password.isEmpty()) {
            passwordLayout.error = "Password is required"
            return false
        }
        if (password.length < 8) {
            passwordLayout.error = "Password minimum 8 characters"
            return false
        }
        passwordLayout.error = ""
        return true
    }

    @SuppressLint("LogNotTimber")
    private fun loginFirebase(email: String, password: String) {
        requireActivity().hideKeyboard()
        CoroutineScope(Dispatchers.Main).launch {
            try {
                loader.show()
                auth.signInWithEmailAndPassword(email, password).awaitTaskCompletable()
                requireActivity().toActivity(MainActivity::class.java, bundleOf(), true)
            } catch (e: Exception) {
                Log.e("loginFirebase", e.message ?: "")
                requireActivity().makeToast("Failed to signin")
            } finally {
                loader.dismiss()
            }
        }
    }

    private fun doRegister() {
        findNavController().navigate(R.id.action_signinFragment_to_signUpFragment)
    }

    private fun doResetPassword() {
        findNavController().navigate(R.id.action_signinFragment_to_forgotPasswordFragment)
    }
}