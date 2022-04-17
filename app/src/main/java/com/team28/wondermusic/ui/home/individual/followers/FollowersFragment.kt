package com.team28.wondermusic.ui.home.individual.followers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.team28.wondermusic.adapter.AccountAdapter
import com.team28.wondermusic.adapter.ItemAccountClickListener
import com.team28.wondermusic.base.BaseDialogFragment
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.data.TempData
import com.team28.wondermusic.data.models.Account
import com.team28.wondermusic.databinding.FragmentFollowersBinding
import com.team28.wondermusic.ui.account.AccountActivity

class FollowersFragment : BaseDialogFragment(), ItemAccountClickListener {

    private lateinit var binding: FragmentFollowersBinding
    private lateinit var accountAdapter: AccountAdapter




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
        accountAdapter.differ.submitList(TempData.accounts)

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