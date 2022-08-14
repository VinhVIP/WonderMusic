package com.vinh.wondermusic.ui.admin.account

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.vinh.wondermusic.R
import com.vinh.wondermusic.adapter.AccountLockAdapter
import com.vinh.wondermusic.adapter.AccountLockListener
import com.vinh.wondermusic.common.Constants
import com.vinh.wondermusic.common.Helper
import com.vinh.wondermusic.data.models.Account
import com.vinh.wondermusic.databinding.ActivityLockAccountBinding
import com.vinh.wondermusic.ui.account.AccountActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LockAccountActivity : AppCompatActivity(), AccountLockListener {

    private lateinit var binding: ActivityLockAccountBinding
    private val viewModel by viewModels<LockAccountViewModel>()

    private lateinit var accountAdapter: AccountLockAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLockAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Helper.setStatusBarGradiant(this, R.drawable.bg_account)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        setupRecyclerAccount()
        observers()

    }

    override fun onResume() {
        super.onResume()
        viewModel.getLocksAccount()
    }

    private fun observers() {
        viewModel.lockAccounts.observe(this) {
            accountAdapter.differ.submitList(it)

            if (it.isEmpty()) {
                binding.tvAlert.visibility = View.VISIBLE
            } else {
                binding.tvAlert.visibility = View.GONE
            }
        }

        viewModel.status.observe(this) {
            it?.let {
                if (it) {
                    viewModel.getLocksAccount()
                    Toast.makeText(this, viewModel.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, viewModel.message, Toast.LENGTH_SHORT).show()
                }
                viewModel.status.postValue(null)
            }
        }
    }

    private fun setupRecyclerAccount() {
        accountAdapter = AccountLockAdapter(this)
        binding.rvAccount.apply {
            adapter = accountAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onAccountClick(account: Account) {
        startActivity(Intent(this, AccountActivity::class.java).apply {
            putExtra(Constants.Account, account)
        })
    }

    override fun onAccountUnlock(account: Account) {
        viewModel.unlockAccount(account)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}