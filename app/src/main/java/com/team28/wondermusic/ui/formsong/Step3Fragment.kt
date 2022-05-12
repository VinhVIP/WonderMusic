package com.team28.wondermusic.ui.formsong

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.team28.wondermusic.adapter.AccountAdapter
import com.team28.wondermusic.adapter.ItemAccountClickListener
import com.team28.wondermusic.common.DataLocal
import com.team28.wondermusic.data.models.Account
import com.team28.wondermusic.databinding.FragmentStep3Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Step3Fragment : Fragment(), ItemAccountClickListener {

    private lateinit var binding: FragmentStep3Binding
    private val viewModel by viewModels<FormSongViewModel>({ requireActivity() })

    private lateinit var singerAdapter: AccountAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStep3Binding.inflate(inflater, container, false)

        singerAdapter = AccountAdapter(this)
        binding.recyclerSingers.apply {
            adapter = singerAdapter
            layoutManager = LinearLayoutManager(context)
        }

        textWatchers()
        observers()

        viewModel.addSingers(DataLocal.myAccount)

        return binding.root
    }

    private fun textWatchers() {
        binding.edSearchSingers.addTextChangedListener {
            it?.let {
                if (it.toString().trim().isNotEmpty()) {
                    viewModel.searchAccount(it.toString())
                }
            }
        }
    }

    private fun observers() {
        viewModel.listAccountSearch.observe(viewLifecycleOwner) {
            singerAdapter.differ.submitList(it)
        }

        viewModel.isLoadingListSingers.observe(viewLifecycleOwner) {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.singers.observe(viewLifecycleOwner) {
            binding.chipGroup.removeAllViews()
            for (account in it) {
                val chip = createSingerChip(account)
                binding.chipGroup.addView(chip)
            }
        }
    }


    override fun onAccountClick(account: Account) {
        viewModel.addSingers(account)
    }

    private fun createSingerChip(account: Account): Chip {
        return Chip(context).apply {
            text = account.accountName
            isCloseIconVisible = account.idAccount != DataLocal.myAccount.idAccount

            setOnCloseIconClickListener {
                viewModel.removeSingers(account)
            }
        }
    }

}