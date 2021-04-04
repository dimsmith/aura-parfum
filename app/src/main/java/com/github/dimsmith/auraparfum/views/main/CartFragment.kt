package com.github.dimsmith.auraparfum.views.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.dimsmith.auraparfum.R
import com.github.dimsmith.auraparfum.common.formatCurrency
import com.github.dimsmith.auraparfum.data.model.Cart
import com.github.dimsmith.auraparfum.data.model.Product
import com.github.dimsmith.auraparfum.views.cart.CartAdapter

class CartFragment : Fragment(), CartAdapter.CartListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var grandTotalText: TextView
    private lateinit var paymentBtn: Button
    private lateinit var cartAdapter: CartAdapter
    private val carts = listOf(
        Cart(Product(1, "Lacoste T-Shirt Premium Original Anti Fake", 10000.0), 1),
        Cart(Product(2, "Product-002", 20000.0), 10),
        Cart(Product(3, "Product-003", 30000.0), 15),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.cart_recycler_view)
        grandTotalText = view.findViewById(R.id.grand_total_txt)
        paymentBtn = view.findViewById(R.id.payment_btn)



        cartAdapter = CartAdapter(requireContext(), this)
        cartAdapter.setItems(carts)
        val myLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.apply {
            layoutManager = myLayoutManager
            adapter = cartAdapter
            setHasFixedSize(true)
        }
        grandTotalText.text = formatCurrency(carts.sumByDouble { (it.qty * it.product.price) })
    }

    override fun onQtyHasChanged(items: List<Cart>) {
        grandTotalText.text = formatCurrency(items.sumByDouble { (it.qty * it.product.price) })
        cartAdapter.setItems(items)
    }
}