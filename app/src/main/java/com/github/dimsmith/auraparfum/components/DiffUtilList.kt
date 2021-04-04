package com.github.dimsmith.auraparfum.components

import androidx.recyclerview.widget.DiffUtil
import com.github.dimsmith.auraparfum.data.model.Cart

class DiffUtilList<T>(private val oldItems: List<T>, private val newItems: List<T>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return getIdentical(oldItemPosition, newItemPosition)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return getIdentical(oldItemPosition, newItemPosition)
    }

    private fun getIdentical(oldPosition: Int, newPosition: Int): Boolean {
        val old = oldItems[oldPosition]
        val new = newItems[newPosition]
        return if (old is Cart && new is Cart) {
            (old.qty == new.qty && old.product.id == new.product.id)
        } else {
            false
        }
    }
}