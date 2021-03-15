package com.github.dimsmith.auraparfum.data.model

import java.io.Serializable

data class Address(
    val addressee: String,
    val phone: String,
    val address: String,
    val city: String,
    val postalCode: String,
    val note: String = "",
    val isPrimary: Boolean
) : Serializable {
    val countryCode: String = "IDN"
    val cityPostalDisplay: String = "$city - $postalCode"
}