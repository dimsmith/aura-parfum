package com.github.dimsmith.auraparfum.views.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.dimsmith.auraparfum.R
import com.github.dimsmith.auraparfum.common.hideKeyboard
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
    private lateinit var phoneCodeInputLayout: TextInputLayout
    private lateinit var noteInputLayout: TextInputLayout
    private lateinit var saveButton: Button

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
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().hideKeyboard()
    }

    private fun initElement(view: View) {
        fullNameInputLayout = view.findViewById(R.id.full_name_input_layout)
        addressInputLayout = view.findViewById(R.id.address_input_layout)
        cityInputLayout = view.findViewById(R.id.city_input_layout)
        postalCodeInputLayout = view.findViewById(R.id.postal_code_input_layout)
        phoneCodeInputLayout = view.findViewById(R.id.phone_input_layout)
        noteInputLayout = view.findViewById(R.id.note_input_layout)
        saveButton = view.findViewById(R.id.save_btn)
        saveButton.setOnClickListener {
            saveAddress()
        }
        if (action == "Update") {
            bindForm()
        }
    }

    private fun bindForm() {
        address?.let { address ->
            fullNameInputLayout.editText?.setText(address.addressee)
            addressInputLayout.editText?.setText(address.address)
            cityInputLayout.editText?.setText(address.city)
            postalCodeInputLayout.editText?.setText(address.postalCode)
            phoneCodeInputLayout.editText?.setText(address.phone)
            noteInputLayout.editText?.setText(address.note)
        }
    }

    private fun saveAddress() {
        // Validation
        requireActivity().makeToast("Saved")
        findNavController().navigateUp()

        // Save Data
    }
}