package com.github.dimsmith.auraparfum.views.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.github.dimsmith.auraparfum.R
import com.github.dimsmith.auraparfum.common.MyResult
import com.github.dimsmith.auraparfum.data.model.User
import com.github.dimsmith.auraparfum.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

class HomeFragment : Fragment() {
    internal class HomeViewModel : ViewModel() {
        private val repo = UserRepository()

        @ExperimentalCoroutinesApi
        val fetchUser = liveData<MyResult<User>>(Dispatchers.IO) {
            val uid = "uJAm75clhahIaGW4MujKhTI87B93"
            try {
                repo.getUser(uid)
                    .collect {
                        when (it) {
                            is MyResult.Failure -> {
                                throw it.ex
                            }
                            is MyResult.Success -> emit(it)
                        }
                    }
            } catch (e: Exception) {
                emit(MyResult.Failure(e))
            }
        }


    }


    private lateinit var viewModel: HomeViewModel
    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        textView = view.findViewById(R.id.textView)
//        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
//        viewModel.fetchUser.observe(viewLifecycleOwner, { myResult ->
//            when (myResult) {
//                is MyResult.Success -> {
//                    textView.text = "${myResult.data.firstName} ${myResult.data.lastName}"
//                }
//                is MyResult.Failure -> requireContext().makeToast(myResult.ex.message ?: "asdasd")
//            }
//
//        })
    }

}