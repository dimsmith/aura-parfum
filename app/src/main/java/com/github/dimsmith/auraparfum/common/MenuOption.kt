package com.github.dimsmith.auraparfum.common

import androidx.annotation.IdRes

data class MenuOption(@IdRes val icon: Int? = null, val key: String, val name: String) {
    val hasIcon = icon != null
}