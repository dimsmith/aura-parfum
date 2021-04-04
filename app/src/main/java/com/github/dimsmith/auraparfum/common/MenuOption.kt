package com.github.dimsmith.auraparfum.common

data class MenuOption(val icon: Int? = null, val key: String, val name: String) {
    val hasIcon = icon != null
}