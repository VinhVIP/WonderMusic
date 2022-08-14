package com.vinh.wondermusic.ui.admin.type.form_type

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.vinh.wondermusic.common.Constants
import com.vinh.wondermusic.data.models.Type
import com.vinh.wondermusic.databinding.FragmentFormTypeBinding
import com.vinh.wondermusic.ui.admin.type.ManageTypeViewModel

class FormTypeFragment : DialogFragment() {

    private lateinit var binding: FragmentFormTypeBinding
    private val viewModel by activityViewModels<ManageTypeViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private var type: Type? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        type = arguments?.getParcelable(Constants.Type)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormTypeBinding.inflate(inflater, container, false)

        setData()
        observers()
        actions()

        return binding.root
    }

    private fun setData() {
        binding.apply {
            if (type != null) {
                edType.setText(type!!.name)

                tvFormTitle.text = "Cập nhật thể loại"
                btnSubmit.text = "Cập nhật"
            } else {
                tvFormTitle.text = "Thêm thể loại"
                btnSubmit.text = "Thêm"
            }
        }
    }

    private fun actions() {
        binding.btnSubmit.setOnClickListener {
            if (type == null) {
                val addType = getTypeModal()
                addType?.let {
                    viewModel.addType(it)
                }
            } else {
                val updateType = getTypeModal()
                updateType?.let {
                    viewModel.updateType(it)
                }
            }
        }
    }

    private fun observers() {
        viewModel.isLoading.observe(this) {
            binding.btnSubmit.isEnabled = !it
            isCancelable = !it
        }

        viewModel.status.observe(this) {
            it?.let {
                if (it) {
                    Toast.makeText(context, viewModel.message, Toast.LENGTH_SHORT).show()
                    dismiss()
                } else {
                    Toast.makeText(context, viewModel.message, Toast.LENGTH_SHORT).show()
                }
                viewModel.status.postValue(null)
            }
        }
    }

    private fun getTypeModal(): Type? {
        if (binding.edType.text.toString().trim().isEmpty()) {
            Toast.makeText(context, "Tên thể loại không được bỏ trống", Toast.LENGTH_SHORT).show()
            return null
        } else {
            type?.let {
                return Type(it.idType, binding.edType.text.toString().trim(), it.description)
            }
        }
        return Type(0, binding.edType.text.toString().trim(), "")
    }
}