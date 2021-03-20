package com.github.dimsmith.auraparfum.views.auth

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.dimsmith.auraparfum.R
import com.github.dimsmith.auraparfum.common.*
import com.github.dimsmith.auraparfum.views.main.MainActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {
    private lateinit var signInBtn: Button
    private lateinit var signUpBtn: Button
    private lateinit var firstNameLayout: TextInputLayout
    private lateinit var lastNameLayout: TextInputLayout
    private lateinit var phoneLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var firstNameInput: EditText
    private lateinit var lastNameInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loader: AlertDialog
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerElement(view)
    }

    private fun registerElement(view: View) {
        signInBtn = view.findViewById(R.id.sign_in_btn)
        signUpBtn = view.findViewById(R.id.sign_up_btn)
        firstNameLayout = view.findViewById(R.id.first_name_txt_layout)
        lastNameLayout = view.findViewById(R.id.last_name_txt_layout)
        phoneLayout = view.findViewById(R.id.phone_number_txt_layout)
        emailLayout = view.findViewById(R.id.email_txt_layout)
        passwordLayout = view.findViewById(R.id.password_txt_layout)
        loader = requireContext().initProgressBar(resources)
        firstNameInput = firstNameLayout.editText!!
        lastNameInput = lastNameLayout.editText!!
        phoneInput = phoneLayout.editText!!
        emailInput = emailLayout.editText!!
        passwordInput = passwordLayout.editText!!

        signInBtn.setOnClickListener { doLogin() }
        signUpBtn.setOnClickListener { doRegister() }
    }

    private fun doLogin() {
        findNavController().navigateUp()
    }

    private fun validate(): Boolean {
        val firstName = firstNameInput.text.toString()
        val lastName = lastNameInput.text.toString()
        val phone = phoneInput.text.toString()
        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()

        if (firstName.isEmpty()) {
            firstNameLayout.error = "First Name is required"
            return false
        }
        firstNameLayout.error = ""
        if (lastName.isEmpty()) {
            lastNameInput.error = "Last Name is required"
            return false
        }
        lastNameLayout.error = ""
        if (phone.isEmpty()) {
            phoneLayout.error = "Phone is required"
            return false
        }
        phoneLayout.error = ""
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
    private fun registerFirebase() {
        requireActivity().hideKeyboard()
        CoroutineScope(Dispatchers.Main).launch {
            loader.show()
            try {
                val defaultAvatar =
                    "gs://auraparfumrework.appspot.com/users/default-user-avatar.png"
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()
                val firstName = firstNameInput.text.toString()
                val lastName = lastNameInput.text.toString()
                val phone = phoneInput.text.toString()
                val auth = auth.createUserWithEmailAndPassword(email, password).await()

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName("$firstName $lastName")
                    .setPhotoUri(Uri.parse(defaultAvatar))
                    .build()

                auth?.user?.let {
                    it.updateProfile(profileUpdates)
                    db.collection("users")
                        .document(it.uid)
                        .collection("additional")
                        .add(
                            mapOf(
                                "first_name" to firstName,
                                "last_name" to lastName,
                                "phone" to phone,
                            )
                        ).await()

                }
                requireActivity().toActivity(MainActivity::class.java, bundleOf(), true)
            } catch (e: Exception) {
                Log.e("registerFirebase", e.message ?: "")
                auth.currentUser?.delete()
                requireActivity().makeToast("Failed to signup")
            } finally {
                loader.dismiss()
            }
        }

    }

    private fun doRegister() {
        if (validate()) {
            registerFirebase()
        }
    }
}