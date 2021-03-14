package com.github.dimsmith.auraparfum.components.bottomsheet

interface BottomSheetListener {
    fun <T> onSelectedItemClick(action: String, item: T)
}