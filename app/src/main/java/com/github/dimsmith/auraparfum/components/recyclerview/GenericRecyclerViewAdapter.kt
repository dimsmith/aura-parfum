package com.github.dimsmith.auraparfum.components.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.dimsmith.auraparfum.components.DiffUtilList

abstract class GenericRecyclerViewAdapter<T, VH : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var _items: MutableList<T> = mutableListOf()

    fun setItems(items: List<T>) {
        val diffUtil = DiffUtilList(_items, items)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        _items = items.toMutableList()
        diffResult.dispatchUpdatesTo(this)
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