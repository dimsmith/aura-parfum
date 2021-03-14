package com.github.dimsmith.auraparfum.components.bottomsheet

import android.content.Context
import android.widget.ArrayAdapter
import com.github.dimsmith.auraparfum.common.MenuOption

class BottomSheetAdapter(ctx: Context, layout: Int, private val menus: List<MenuOption>) :
    ArrayAdapter<MenuOption>(ctx, layout) {
    override fun getCount(): Int = menus.size
}