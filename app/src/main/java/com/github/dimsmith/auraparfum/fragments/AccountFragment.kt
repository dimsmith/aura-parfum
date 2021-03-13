package com.github.dimsmith.auraparfum.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.github.dimsmith.auraparfum.R
import com.github.dimsmith.auraparfum.common.recyclerview.GenericRecyclerViewAdapter
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage

class AccountFragment : Fragment() {
    private lateinit var listView: RecyclerView
    private lateinit var profileImage: ImageView
    private lateinit var profileImageShimmer: ShimmerFrameLayout
    private val fireStorage = FirebaseStorage.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    private val avatarUrl = "gs://auraparfumrework.appspot.com/users/avatar1.jpg"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileImage = view.findViewById(R.id.profile_img)
        profileImageShimmer = view.findViewById(R.id.profile_img_shimmer)
        profileImageShimmer.startShimmer()
        val ref = fireStorage.getReferenceFromUrl(avatarUrl)
        ref.downloadUrl.addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result != null) {
                    it.result?.let {
                        val url = it.toString()
                        Glide.with(requireContext())
                            .load(url)
                            .circleCrop()
                            .into(profileImage)
                        profileImageShimmer.stopShimmer()
                        profileImageShimmer.hideShimmer()
                        profileImage.setBackgroundColor(resources.getColor(R.color.transparant))
                    }
                }
            }
        }



//        listView = view.findViewById(R.id.account_list_view)
//        val data = arrayListOf<String>()
//
//        for (i in 1..10) data.add("Menu #$i")
//
//        val adapter = object : GenericRecyclerViewAdapter<String>() {
//            override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
//                return MenuViewHolder(view)
//            }
//            override fun getLayout(position: Int, item: String): Int = R.layout.layout_menu_list
//        }
//        adapter.setItems(data)
//        listView.layoutManager= LinearLayoutManager(requireContext())
//        listView.setHasFixedSize(true)
//        listView.adapter = adapter
    }


    internal class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), GenericRecyclerViewAdapter.Binder<String> {
        private val itemTitleText: TextView = itemView.findViewById(R.id.item_title)

        override fun bind(data: String) {
            itemTitleText.text = data
        }
    }
}