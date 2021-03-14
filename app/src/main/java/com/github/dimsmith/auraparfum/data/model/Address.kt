package com.github.dimsmith.auraparfum.data.model

data class Address(
    val addressee: String,
    val phone: String,
    val address: String,
    val city: String,
    val postalCode: String,
    val notes: String = "",
    val isPrimary: Boolean
) {
    val countryCode: String = "IDN"
    val cityPostalDisplay: String = "$city - $postalCode"
}