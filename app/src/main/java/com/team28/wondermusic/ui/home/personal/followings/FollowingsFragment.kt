package com.team28.wondermusic.ui.home.personal.followings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.team28.wondermusic.adapter.AccountAdapter
import com.team28.wondermusic.adapter.ItemAccountClickListener
import com.team28.wondermusic.base.fragments.BaseDialogFragment
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.common.DataLocal
import com.team28.wondermusic.data.models.Account
import com.team28.wondermusic.databinding.FragmentFollowingsBinding
import com.team28.wondermusic.ui.account.AccountActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingsFragment : BaseDialogFragment(), ItemAccountClickListener {

    private lateinit var binding: FragmentFollowingsBinding
    private val viewModel by viewModels<FollowingsViewModel>()
    private lateinit var accountAdapter: AccountAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getFollowings(DataLocal.myAccount.idAccount)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountAdapter = AccountAdapter(this)

        viewModel.isLoading.observe(this) {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.followings.observe(this) {
            accountAdapter.differ.submitList(it)
        }

        binding.recyclerAccount.apply {
            adapter = accountAdapter
            layoutManager = LinearLayoutManager(this@FollowingsFragment.context)
        }
    }

    override fun onAccountClick(account: Account) {
        startActivity(Intent(context, AccountActivity::class.java).apply {
            putExtra(Constants.Account, account)
        })
    }
}