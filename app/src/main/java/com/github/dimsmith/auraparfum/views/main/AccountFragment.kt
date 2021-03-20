package com.github.dimsmith.auraparfum.views.main

import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.dimsmith.auraparfum.R
import com.github.dimsmith.auraparfum.common.MenuOption
import com.github.dimsmith.auraparfum.common.showAlertDialog
import com.github.dimsmith.auraparfum.common.toActivity
import com.github.dimsmith.auraparfum.components.recyclerview.GenericListViewAdapter
import com.github.dimsmith.auraparfum.views.address.AddressActivity
import com.github.dimsmith.auraparfum.views.auth.AuthActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class AccountFragment : Fragment() {
    private val fireStorage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        setupProfile(view)
        setupAccountMenu(view)
        return view
    }

    private fun setupProfile(view: View) {
        // Photo profile
        val userFirebase = auth.currentUser!!
        val profileImage: ImageView = view.findViewById(R.id.profile_img)
        val ref = fireStorage.getReferenceFromUrl(userFirebase.photoUrl?.toString()!!)
        Glide.with(requireContext())
            .load(ref)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .circleCrop()
            .into(profileImage)
        val fullName: TextView = view.findViewById(R.id.full_name)
        val email: TextView = view.findViewById(R.id.email)
        fullName.text = userFirebase.displayName
        email.text = userFirebase.email

    }

    private fun setupAccountMenu(view: View) {
        val listView: ListView = view.findViewById(R.id.account_menu_list_view)
        val menus = arrayListOf(
            MenuOption(R.drawable.ic_cart, "orders", "My Orders"),
            MenuOption(R.drawable.ic_location, "addresses", "My Addresses"),
            MenuOption(R.drawable.ic_setting, "settings", "Account Settings"),
            MenuOption(R.drawable.ic_logout, "logout", "Logout"),
        )
        val adapter = object : GenericListViewAdapter<MenuOption>() {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun getParentView(item: MenuOption, parent: ViewGroup?): View {
                val v = LayoutInflater.from(parent?.context)
                    .inflate(R.layout.layout_menu_list, parent, false)
                val icon: ImageView = v.findViewById(R.id.item_icon)
                val title: TextView = v.findViewById(R.id.item_title)
                if (!item.hasIcon) icon.visibility = View.GONE
                else {
                    val iconRaw = Icon.createWithResource(requireContext(), item.icon!!)
                    icon.setImageIcon(iconRaw)
                }
                title.text = item.name
                return v
            }
        }
        adapter.setItems(menus)
        listView.adapter = adapter
        listView.setOnItemClickListener { _, _, position, _ ->
            onMenuClicked(menus[position])
        }
    }

    private fun onMenuClicked(menuOption: MenuOption) {
        when (menuOption.key) {
            "addresses" -> {
                requireContext().toActivity(AddressActivity::class.java)
            }
            "logout" -> requestLogout()
        }
    }

    private fun requestLogout() {
        requireContext().showAlertDialog("Are you sure, want to logout?") { _, _ ->
            auth.signOut()
            requireActivity().toActivity(AuthActivity::class.java, bundleOf(), true)
        }
    }
}