package com.github.dimsmith.auraparfum.views.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.dimsmith.auraparfum.R
import com.github.dimsmith.auraparfum.components.recyclerview.GenericListViewAdapter

class CartFragment : Fragment() {
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = view.findViewById(R.id.cart_list_view)

        val data = arrayListOf<String>()
        for (i in 0..10) {
            data.add("data #$i")
        }
        val adapter = object : GenericListViewAdapter<String>() {
            override fun getParentView(item: String, parent: ViewGroup?): View {
                val v = LayoutInflater.from(parent?.context).inflate(R.layout.layout_menu_list, parent, false)
                val t : TextView = v.findViewById(R.id.item_title)
                t.text = item
                return v
            }

        }

        adapter.setItems(data)
        listView.adapter = adapter
    }
}