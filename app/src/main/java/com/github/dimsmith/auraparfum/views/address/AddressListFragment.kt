package com.github.dimsmith.auraparfum.views.address

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.dimsmith.auraparfum.R
import com.github.dimsmith.auraparfum.common.Constanta.DEFAULT_ERROR_MESSAGE
import com.github.dimsmith.auraparfum.common.MenuOption
import com.github.dimsmith.auraparfum.common.MyResult
import com.github.dimsmith.auraparfum.common.makeToast
import com.github.dimsmith.auraparfum.common.showAlertDialog
import com.github.dimsmith.auraparfum.components.bottomsheet.BottomSheetFragment
import com.github.dimsmith.auraparfum.components.bottomsheet.BottomSheetListener
import com.github.dimsmith.auraparfum.components.recyclerview.GenericRecyclerViewAdapter
import com.github.dimsmith.auraparfum.components.recyclerview.MyDividerRecyclerView
import com.github.dimsmith.auraparfum.data.model.Address
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.ExperimentalCoroutinesApi

class AddressListFragment : Fragment(), BottomSheetListener {
    private lateinit var viewModel: AddressViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_address_list, container, false)
        setupAddAddress(view)
        return view
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddressViewModel::class.java)
        viewModel.fetchAddress.observe(viewLifecycleOwner) { myResult ->
            when(myResult) {
                is MyResult.Success -> {
                    setupAddressList(view, myResult.data)
                }
                is MyResult.Failure -> requireContext().makeToast(myResult.ex.message ?: DEFAULT_ERROR_MESSAGE)
            }
        }
    }

    private fun setupAddressList(view: View, addresses: List<Address>) {
        val recyclerView: RecyclerView = view.findViewById(R.id.address_recycler_view)
        val adapter = object : GenericRecyclerViewAdapter<Address, AddressViewHolder>() {
            override fun getLayout(position: Int, item: Address): Int {
                return R.layout.layout_address_list
            }

            override fun getViewHolder(view: View, viewType: Int): AddressViewHolder {
                return AddressViewHolder(view)
            }

            @SuppressLint("SetTextI18n")
            override fun onBindViewHolderParent(holder: AddressViewHolder, item: Address) {
                holder.addresseeText.text = item.addressee
                holder.addressText.text = item.address
                holder.cityPostalText.text = "${item.city} - ${item.postalCode}"
                holder.phoneText.text = item.phone
                item.isPrimary?.let {
                    holder.isPrimaryText.visibility = if (it) View.VISIBLE else View.GONE
                }
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
        item.isPrimary?.let {
            if (!it) availableMenus.add(0, MenuOption(R.drawable.ic_done, "primary", "Make Primary"))
        }
        availableMenus.add(MenuOption(R.drawable.ic_update, "update", "Update"))
        availableMenus.add(MenuOption(R.drawable.ic_trash, "delete", "Delete"))
        BottomSheetFragment.getInstance(availableMenus, this, item, "Action")
            .show(requireActivity().supportFragmentManager, BottomSheetFragment.TAG)
    }

    private fun setupAddAddress(view: View) {
        val fab: FloatingActionButton = view.findViewById(R.id.add_address_fab)
        fab.setOnClickListener {
            findNavController().navigate(
                R.id.action_addressListFragment_to_addressFormFragment, bundleOf(
                    "action" to "Add"
                )
            )
        }
    }

    override fun <T> onSelectedItemClick(action: String, item: T) {
        when (item) {
            is Address -> {
                when (action) {
                    "update" -> {
                        findNavController().navigate(
                            R.id.action_addressListFragment_to_addressFormFragment, bundleOf(
                                "action" to "Update",
                                "address" to item
                            )
                        )
                    }
                    "delete" -> {
                        requireContext().showAlertDialog(
                            "Are you sure, want to delete this address?"
                        ) { dialog, which -> requireContext().makeToast("DELETED") }
                    }
                    else -> {
                        val tmp = "$action ${item.address}"
                        requireContext().makeToast(tmp)
                    }
                }
            }
        }
    }
}