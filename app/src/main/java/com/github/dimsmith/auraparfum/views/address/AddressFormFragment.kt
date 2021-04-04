package com.github.dimsmith.auraparfum.views.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.dimsmith.auraparfum.R
import com.github.dimsmith.auraparfum.common.Constanta.DEFAULT_ERROR_MESSAGE
import com.github.dimsmith.auraparfum.common.hideKeyboard
import com.github.dimsmith.auraparfum.common.initProgressBar
import com.github.dimsmith.auraparfum.common.makeToast
import com.github.dimsmith.auraparfum.data.model.Address
import com.google.android.material.textfield.TextInputLayout

class AddressFormFragment : Fragment() {
    private lateinit var action: String
    private var address: Address? = null
    private lateinit var fullNameInputLayout: TextInputLayout
    private lateinit var addressInputLayout: TextInputLayout
    private lateinit var cityInputLayout: TextInputLayout
    private lateinit var postalCodeInputLayout: TextInputLayout
    private lateinit var phoneInputLayout: TextInputLayout
    private lateinit var noteInputLayout: TextInputLayout
    private lateinit var saveButton: Button
    private lateinit var viewModel: AddressViewModel
    private lateinit var loader: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            action = it.getString("action", "add")
            it.getSerializable("address")?.let { serializable ->
                address = serializable as Address
            }
        }
        return inflater.inflate(R.layout.fragment_address_form, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initElement(view)
        viewModel = ViewModelProvider(this).get(AddressViewModel::class.java)
        viewModel.loader.observe(viewLifecycleOwner) {
            if (it) loader.show()
            else loader.dismiss()
        }
        viewModel.exception.observe(viewLifecycleOwner) {
            requireActivity().makeToast(it.message ?: DEFAULT_ERROR_MESSAGE)
        }
        viewModel.onCompleted.observe(viewLifecycleOwner) {
            requireActivity().makeToast(it)
            findNavController().navigateUp()
        }
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().hideKeyboard()
    }

    private fun initElement(view: View) {
        loader = requireContext().initProgressBar(resources)
        fullNameInputLayout = view.findViewById(R.id.full_name_input_layout)
        addressInputLayout = view.findViewById(R.id.address_input_layout)
        cityInputLayout = view.findViewById(R.id.city_input_layout)
        postalCodeInputLayout = view.findViewById(R.id.postal_code_input_layout)
        phoneInputLayout = view.findViewById(R.id.phone_input_layout)
        noteInputLayout = view.findViewById(R.id.note_input_layout)
        saveButton = view.findViewById(R.id.save_btn)
        saveButton.setOnClickListener {
            saveAddress()
        }
        if (action == "Update") {
            bindForm()
        }

        // Clear validation when focused
        arrayListOf(
            fullNameInputLayout,
            phoneInputLayout,
            addressInputLayout,
            cityInputLayout,
            postalCodeInputLayout
        ).forEach { til ->
            til.editText?.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    til.error = ""
                }
            }
        }
    }

    private fun bindForm() {
        address?.let { address ->
            fullNameInputLayout.editText?.setText(address.addressee)
            addressInputLayout.editText?.setText(address.address)
            cityInputLayout.editText?.setText(address.city)
            postalCodeInputLayout.editText?.setText(address.postalCode.toString())
            phoneInputLayout.editText?.setText(address.phone)
            noteInputLayout.editText?.setText(address.note)
        }
    }

    private fun saveAddress() {
        // Validation
        val addresses = fullNameInputLayout.editText!!.text.toString()
        val phone = phoneInputLayout.editText!!.text.toString()
        val address = addressInputLayout.editText!!.text.toString()
        val city = cityInputLayout.editText!!.text.toString()
        val postalCode = postalCodeInputLayout.editText!!.text.toString()
        val note = noteInputLayout.editText!!.text.toString()
        if (addresses.isEmpty()) {
            fullNameInputLayout.error = "Full name is required"
            return
        }
        if (address.isEmpty()) {
            addressInputLayout.error = "Address is required"
            return
        }
        if (city.isEmpty()) {
            cityInputLayout.error = "City is required"
            return
        }
        if (postalCode.isEmpty()) {
            postalCodeInputLayout.error = "Postal code is required"
            return
        }
        if (phone.isEmpty()) {
            phoneInputLayout.error = "Phone is required"
            return
        }



        val _address = Address(addresses, phone, address, city, postalCode.toLong(), note, "IDN", false)
        viewModel.addAddress(_address)
    }
}