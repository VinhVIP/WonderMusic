package com.vinh.wondermusic.ui.forgetpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vinh.wondermusic.data.models.ForgetChangeModal
import com.vinh.wondermusic.databinding.FragmentStepChangeBinding

class StepChangeFragment : Fragment() {

    private lateinit var binding: FragmentStepChangeBinding
    private val viewModel by viewModels<ForgetPasswordViewModel>({ requireActivity() })

    private var isPassValid = false
    private var isConfirmPassValid = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStepChangeBinding.inflate(inflater, container, false)

        textWatchers()

        binding.btnChangePassword.setOnClickListener {
            val modal = getChangeModal()
            modal?.let {
                viewModel.forgetChange(it)
            }
        }

        viewModel.loadingChange.observe(viewLifecycleOwner) {
            binding.btnChangePassword.isEnabled = !it
        }

        viewModel.changeStatus.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    Toast.makeText(context, viewModel.message, Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                } else {
                    Toast.makeText(context, viewModel.message, Toast.LENGTH_SHORT).show()
                }
                viewModel.changeStatus.value = null
            }
        }

        return binding.root
    }

    private fun textWatchers(){
        validatePass()
        validateConfirmPass()
    }

    private fun getChangeModal(): ForgetChangeModal? {
        if (isPassValid && isConfirmPassValid) {
            return ForgetChangeModal(
                viewModel.email,
                viewModel.code,
                binding.edPass.text.toString().trim()
            )
        }
        return null
    }

    private fun validatePass() {
        binding.edPass.addTextChangedListener {
            it?.let {
                binding.tvPassError.apply {
                    isPassValid = false
                    when {
                        it.toString().length < 6 -> {
                            text = "Mật khẩu phái có ít nhất 6 kí tự!"
                            visibility = View.VISIBLE
                        }
                        else -> {
                            isPassValid = true
                            visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun validateConfirmPass() {
        binding.edConfirmPass.addTextChangedListener {
            it?.let {
                binding.tvConfirmPassError.apply {
                    isConfirmPassValid = false
                    when {
                        it.toString().isEmpty() -> {
                            text = "Mật khẩu xác nhận không được bỏ trống!"
                            visibility = View.VISIBLE
                        }
                        it.toString() != binding.edPass.text.toString() -> {
                            text = "Mật khẩu xác nhận không khớp!"
                            visibility = View.VISIBLE
                        }
                        else -> {
                            isConfirmPassValid = true
                            visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

}