package com.github.dimsmith.auraparfum.views.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.dimsmith.auraparfum.R
import com.github.dimsmith.auraparfum.common.MenuOption
import com.github.dimsmith.auraparfum.common.makeToast
import com.github.dimsmith.auraparfum.components.bottomsheet.BottomSheetFragment
import com.github.dimsmith.auraparfum.components.bottomsheet.BottomSheetListener
import com.github.dimsmith.auraparfum.components.recyclerview.GenericRecyclerViewAdapter
import com.github.dimsmith.auraparfum.components.recyclerview.MyDividerRecyclerView
import com.github.dimsmith.auraparfum.data.model.Address

class AddressListFragment : Fragment(), BottomSheetListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_address_list, container, false)
        setupAddressList(view)
        return view
    }

    private fun setupAddressList(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.address_recycler_view)

        val addresses = arrayListOf<Address>()
        for (i in 0..3) {
            val isPrimary = i == 2
            addresses.add(
                Address(
                    "Addressee #$i",
                    "Phone #$i",
                    "Address #$i",
                    "City #$i",
                    "Postal Code #$i",
                    "Notes #$i",
                    isPrimary
                )
            )
        }
        val adapter = object : GenericRecyclerViewAdapter<Address, AddressViewHolder>() {
            override fun getLayout(position: Int, item: Address): Int {
                return R.layout.layout_address_list
            }

            override fun getViewHolder(view: View, viewType: Int): AddressViewHolder {
                return AddressViewHolder(view)
            }

            override fun onBindViewHolderParent(holder: AddressViewHolder, item: Address) {
                holder.addresseeText.text = item.addressee
                holder.addressText.text = item.address
                holder.cityPostalText.text = item.cityPostalDisplay
                holder.phoneText.text = item.phone
                holder.isPrimaryText.visibility = if (item.isPrimary) View.VISIBLE else View.GONE
            }

            override fun onClick(item: Address) {
                showAddressBottomSheet(item)
            }
        }
        adapter.setItems(addresses)


        val layoutManager = LinearLayoutManager(requireContext())
        val itemDivider = MyDividerRecyclerView(requireContext(), layoutManager.orientation)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(itemDivider)
    }

    private fun showAddressBottomSheet(item: Address) {
        val availableMenus = mutableListOf<MenuOption>()
        if (!item.isPrimary) availableMenus.add(
            0,
            MenuOption(R.drawable.ic_home, "primary", "Make Primary")
        )
        availableMenus.add(MenuOption(R.drawable.ic_update, "update", "Update"))
        availableMenus.add(MenuOption(R.drawable.ic_trash, "delete", "Delete"))
        BottomSheetFragment.getInstance(availableMenus, this, item, "Action")
            .show(requireActivity().supportFragmentManager, BottomSheetFragment.TAG)
    }

    override fun <T> onSelectedItemClick(action: String, item: T) {
        when (item) {
            is Address -> {
                val tmp = "$action ${item.address}"
                requireContext().makeToast(tmp)

            }
        }
    }
}