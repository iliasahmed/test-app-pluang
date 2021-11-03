package com.iliasahmed.testpluang.ui.account

import android.annotation.SuppressLint
import com.bumptech.glide.Glide
import com.iliasahmed.testpluang.R
import com.iliasahmed.testpluang.controller.AppController
import com.iliasahmed.testpluang.databinding.FragmentAccountBinding
import com.iliasahmed.testpluang.ui.base.BaseFragment
import com.iliasahmed.testpluang.utils.ViewDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountBinding, AccountViewModel>(
    AccountViewModel::class.java,
    R.layout.fragment_account
) {
    private var alert: ViewDialog? = null

    override fun initViews() {
        alert = ViewDialog(activity)
        binding.tvName.text = viewModel.getName()
        binding.tvEmail.text = viewModel.getEmail()

        Glide
            .with(requireContext())
            .load(viewModel.getImage())
            .error(R.drawable.user_image)
            .into(binding.ivImage)

        binding.tvLogout.setOnClickListener {
            AppController.onLogoutEvent()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun liveEventsObservers() {

    }

    override fun setupRecycler() {

    }

    override fun clickListeners() {

    }
}