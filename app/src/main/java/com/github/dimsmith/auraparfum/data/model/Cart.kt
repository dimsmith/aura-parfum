package com.github.dimsmith.auraparfum.data.model

import java.io.Serializable

data class Cart(val product: Product, var qty: Int) : Serializable