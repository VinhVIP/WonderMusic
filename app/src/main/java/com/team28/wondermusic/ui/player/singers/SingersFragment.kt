package com.team28.wondermusic.ui.player.singers

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
import com.team28.wondermusic.data.models.Account
import com.team28.wondermusic.databinding.FragmentSingersBinding
import com.team28.wondermusic.ui.account.AccountActivity

class SingersFragment : BaseDialogFragment(), ItemAccountClickListener {

    private lateinit var binding: FragmentSingersBinding
    private lateinit var accountAdapter: AccountAdapter

    private var singers: ArrayList<Account> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        singers =
            arguments?.getParcelableArrayList<Account>(Constants.Singers) as ArrayList<Account>
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountAdapter = AccountAdapter(this)
        accountAdapter.differ.submitList(singers)

        binding.recyclerAccount.apply {
            adapter = accountAdapter
            layoutManager = LinearLayoutManager(this@SingersFragment.context)
        }
    }

    override fun onAccountClick(account: Account) {
        startActivity(Intent(context, AccountActivity::class.java).apply {
            putExtra(Constants.Account, account)
        })
    }

}