package com.vinh.wondermusic.ui.home.personal.followers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vinh.wondermusic.adapter.AccountAdapter
import com.vinh.wondermusic.adapter.ItemAccountClickListener
import com.vinh.wondermusic.base.fragments.BaseDialogFragment
import com.vinh.wondermusic.common.Constants
import com.vinh.wondermusic.common.DataLocal
import com.vinh.wondermusic.data.models.Account
import com.vinh.wondermusic.databinding.FragmentFollowersBinding
import com.vinh.wondermusic.ui.account.AccountActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowersFragment : BaseDialogFragment(), ItemAccountClickListener {

    private lateinit var binding: FragmentFollowersBinding
    private val viewModel by viewModels<FollowersViewModel>()
    private lateinit var accountAdapter: AccountAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getFollowers(DataLocal.myAccount.idAccount)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountAdapter = AccountAdapter(this)

        viewModel.isLoading.observe(this) {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.followers.observe(this) {
            accountAdapter.differ.submitList(it)
            if (it.isNotEmpty()) {
                binding.recyclerAccount.visibility = View.VISIBLE
                binding.tvAlert.visibility = View.GONE
            } else {
                binding.recyclerAccount.visibility = View.GONE
                binding.tvAlert.visibility = View.VISIBLE
            }
        }

        binding.recyclerAccount.apply {
            adapter = accountAdapter
            layoutManager = LinearLayoutManager(this@FollowersFragment.context)
        }
    }

    override fun onAccountClick(account: Account) {
        startActivity(Intent(context, AccountActivity::class.java).apply {
            putExtra(Constants.Account, account)
        })
    }


}