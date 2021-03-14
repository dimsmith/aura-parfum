package com.github.dimsmith.auraparfum.views.address

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.dimsmith.auraparfum.R

class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val addresseeText: TextView = itemView.findViewById(R.id.addressee_text)
    val addressText: TextView = itemView.findViewById(R.id.address_text)
    val cityPostalText: TextView = itemView.findViewById(R.id.city_postal_text)
    val phoneText: TextView = itemView.findViewById(R.id.phone_text)
    val isPrimaryText: TextView = itemView.findViewById(R.id.is_primary_text)
}