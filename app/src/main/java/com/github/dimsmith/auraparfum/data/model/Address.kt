package com.github.dimsmith.auraparfum.data.model

import java.io.Serializable

data class Address(
    val addressee: String? = null,
    val phone: String? = null,
    val address: String? = null,
    @JvmField
    val city: String? = null,
    @JvmField
    val postalCode: Long? = null,
    val note: String = "",
    val countryCode: String? = null,
    @JvmField
    val isPrimary: Boolean? = null
) : Serializable