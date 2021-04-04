package com.github.dimsmith.auraparfum.views.cart

import android.view.View
import com.github.dimsmith.auraparfum.R
import com.github.dimsmith.auraparfum.common.formatCurrency
import com.github.dimsmith.auraparfum.components.recyclerview.GenericRecyclerViewAdapter
import com.github.dimsmith.auraparfum.data.model.Cart

class CartAdapter(private val listener: CartListener) :
    GenericRecyclerViewAdapter<Cart, CartViewHolder>() {

    interface CartListener {
        fun onQtyHasChanged(cart: Cart, isIncrement: Boolean = false)
    }

    fun getItems() = _items

    override fun getLayout(position: Int, item: Cart): Int = R.layout.layout_cart_item

    override fun getViewHolder(view: View, viewType: Int): CartViewHolder {
        return CartViewHolder(view)
    }

    override fun onBindViewHolderParent(holder: CartViewHolder, item: Cart) {
        holder.productName.text = item.product.name
        holder.productPrice.text = formatCurrency(item.product.price)
        holder.productQty.setText(item.qty.toString())
        holder.decrementBtn.setOnClickListener {
            listener.onQtyHasChanged(item)
        }
        holder.incrementBtn.setOnClickListener {
            listener.onQtyHasChanged(item, true)
        }
    }

    override fun onClick(item: Cart) {
    }
}