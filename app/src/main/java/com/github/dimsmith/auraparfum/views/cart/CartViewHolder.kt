package com.github.dimsmith.auraparfum.views.cart

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.dimsmith.auraparfum.R
import com.google.android.material.textfield.TextInputEditText

class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val productImage: ImageView = itemView.findViewById(R.id.cart_product_img)
    val productName: TextView = itemView.findViewById(R.id.cart_product_name_txt)
    val productPrice: TextView = itemView.findViewById(R.id.cart_product_price_txt)
    val productTotal: TextView = itemView.findViewById(R.id.cart_product_total_txt)
    val productQty: TextInputEditText = itemView.findViewById(R.id.cart_product_qty_input)
    val decrementBtn: ImageButton = itemView.findViewById(R.id.decrement_qty)
    val incrementBtn: ImageButton = itemView.findViewById(R.id.increment_qty)
}