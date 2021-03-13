package com.github.dimsmith.auraparfum.common.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.github.dimsmith.auraparfum.R

abstract class GenericListViewAdapter<T> : BaseAdapter() {
    private var _items = listOf<T>()
    fun setItems(items: List<T>) {
        this._items = items
        notifyDataSetChanged()
    }

    override fun getCount(): Int = _items.size

    override fun getItem(position: Int): T = _items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getParentView(_items[position], parent)
    }

    abstract fun getParentView(item: T, parent: ViewGroup?) : View
}