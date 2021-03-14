package com.github.dimsmith.auraparfum.data.model

data class User(val uid: String, val firstName: String, val lastName: String, val email: String) {
    val fullName: String = "$firstName $lastName"
}
