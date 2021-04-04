package com.github.dimsmith.auraparfum.views.cart

import android.content.Context
import android.view.View
import com.github.dimsmith.auraparfum.R
import com.github.dimsmith.auraparfum.common.formatCurrency
import com.github.dimsmith.auraparfum.common.showAlertDialog
import com.github.dimsmith.auraparfum.components.recyclerview.GenericRecyclerViewAdapter
import com.github.dimsmith.auraparfum.data.model.Cart

class CartAdapter(private val ctx: Context, private val listener: CartListener) :
    GenericRecyclerViewAdapter<Cart, CartViewHolder>() {

    interface CartListener {
        fun onQtyHasChanged(items: List<Cart>)
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
            changeItemQty(item, holder)
        }
        holder.incrementBtn.setOnClickListener {
            changeItemQty(item, holder, true)
        }
        holder.productQty.setText(item.qty.toString())

    }

    private fun changeItemQty(item: Cart, holder: CartViewHolder, increase: Boolean = false) {
        val newItems = arrayListOf<Cart>()
        newItems.addAll(_items)
        if (increase) item.qty += 1
        else {
            if (item.qty == 1) {
                ctx.showAlertDialog("Are you sure want to remove this item from your cart?") { _, _ ->
                    newItems.remove(item)
                    listener.onQtyHasChanged(newItems)
                }
                return
            } else item.qty -= 1
        }
        holder.productQty.setText(item.qty.toString())
        listener.onQtyHasChanged(newItems)
    }

    override fun onClick(item: Cart) {
    }
}