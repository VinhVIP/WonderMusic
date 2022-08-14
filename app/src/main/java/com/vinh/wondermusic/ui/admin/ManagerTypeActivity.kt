package com.vinh.wondermusic.ui.admin

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vinh.wondermusic.R
import com.vinh.wondermusic.adapter.TypeManageAdapter
import com.vinh.wondermusic.adapter.TypeManageListener
import com.vinh.wondermusic.base.activities.BaseActivity
import com.vinh.wondermusic.base.dialogs.ConfirmDialog
import com.vinh.wondermusic.common.Constants
import com.vinh.wondermusic.common.Helper
import com.vinh.wondermusic.common.SimpleDividerItemDecoration
import com.vinh.wondermusic.data.models.Type
import com.vinh.wondermusic.databinding.ActivityManagerTypeBinding
import com.vinh.wondermusic.ui.admin.form_type.FormTypeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagerTypeActivity : BaseActivity(), TypeManageListener {

    private lateinit var binding: ActivityManagerTypeBinding
    private lateinit var typeAdapter: TypeManageAdapter

    private val viewModel by viewModels<ManageTypeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Helper.setStatusBarGradiant(this, R.drawable.bg_account)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewModel.getTypes()

        setupRecyclerType()
        observers()
    }

    private fun observers() {
        viewModel.types.observe(this) {
            typeAdapter.differ.submitList(it)
        }

        viewModel.status.observe(this) {
            it?.let {
                if (it) {
                    viewModel.getTypes()
                    Toast.makeText(this, viewModel.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, viewModel.message, Toast.LENGTH_SHORT).show()
                }
                viewModel.status.postValue(null)
            }
        }
    }

    private fun setupRecyclerType() {
        typeAdapter = TypeManageAdapter(this)
        binding.rvType.apply {
            adapter = typeAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SimpleDividerItemDecoration(context, R.drawable.divider))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
            R.id.actionAdd -> {
                FormTypeFragment().show(supportFragmentManager, null)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onTypeEdit(type: Type, position: Int) {
        FormTypeFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Type, type)
            }
        }.show(supportFragmentManager, null)
    }

    override fun onTypeDelete(type: Type, position: Int) {
        showConfirmDialog(
            "Xóa thể loại",
            "Bạn chắc chắn muốn xóa thể loại này?",
            "Xóa",
            "Hủy",
            "",
            object : ConfirmDialog.ConfirmCallback {
                override fun negativeAction() {
                }

                override fun positiveAction() {
                    viewModel.deleteType(type)
                }
            }
        )
    }

}