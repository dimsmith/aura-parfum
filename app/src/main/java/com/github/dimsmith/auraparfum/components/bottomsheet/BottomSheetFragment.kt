package com.github.dimsmith.auraparfum.components.bottomsheet

import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.github.dimsmith.auraparfum.R
import com.github.dimsmith.auraparfum.common.MenuOption
import com.github.dimsmith.auraparfum.components.recyclerview.GenericListViewAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment<T>(
    private val menus: MutableList<MenuOption>,
    private val onItem: T,
    private val listener: BottomSheetListener,
    private val title: String
) :
    BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.layout_menu_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.bottom_sheet_title).apply {
            text = title
        }

        val adapter = object : GenericListViewAdapter<MenuOption>() {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun getParentView(item: MenuOption, parent: ViewGroup?): View {
                val v = LayoutInflater.from(parent?.context)
                    .inflate(R.layout.layout_menu_item_bottom_sheet, parent, false)
                val i: ImageView = v.findViewById(R.id.item_icon)
                val t: TextView = v.findViewById(R.id.item_title)
                if (!item.hasIcon) i.visibility = View.GONE
                else {
                    i.setImageIcon(Icon.createWithResource(requireContext(), item.icon!!))
                }

                t.text = item.name
                return v
            }
        }
        menus.add(menus.lastIndex + 1, MenuOption(R.drawable.ic_close, "cancel", "Cancel"))
        adapter.setItems(menus)
        val bottomListView: ListView = view.findViewById(R.id.list_bottom_sheet)
        bottomListView.adapter = adapter
        bottomListView.setOnItemClickListener { _, _, position, _ ->
            listener.onSelectedItemClick(menus[position].key, onItem)
            this.dismiss()
        }

    }

    companion object {
        const val TAG = "BottomSheetFragment"
        fun <T> getInstance(
            menus: MutableList<MenuOption>,
            listener: BottomSheetListener,
            onItem: T,
            title: String = "Other"
        ) =
            BottomSheetFragment(menus, onItem, listener, title)
    }
}