package com.github.dimsmith.auraparfum.common.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class GenericRecyclerViewAdapter<T> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var _items: List<T> = arrayListOf()

    internal interface Binder<T> {
        fun bind(data: T)
    }

    fun setItems(items: List<T>) {
        this._items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false), viewType
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Binder<T>).bind(_items[position])
    }

    override fun getItemViewType(position: Int): Int {
        return getLayout(position, _items[position])
    }


    override fun getItemCount(): Int = this._items.size

    protected abstract fun getLayout(position: Int, item: T): Int

    abstract fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder
}