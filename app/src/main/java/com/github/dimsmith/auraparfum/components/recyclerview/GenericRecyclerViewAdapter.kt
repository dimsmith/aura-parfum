package com.github.dimsmith.auraparfum.components.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class GenericRecyclerViewAdapter<T, VH : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var _items: List<T> = arrayListOf()

    fun setItems(items: List<T>) {
        this._items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        view.setOnClickListener {
            val position = (parent as RecyclerView).getChildLayoutPosition(it)
            onClick(_items[position])
        }
        return getViewHolder(view, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return getLayout(position, _items[position])
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolderParent(holder as VH, _items[position])
    }

    override fun getItemCount(): Int = this._items.size

    protected abstract fun getLayout(position: Int, item: T): Int

    abstract fun getViewHolder(view: View, viewType: Int): VH

    abstract fun onBindViewHolderParent(holder: VH, item: T)

    abstract fun onClick(item: T)
}