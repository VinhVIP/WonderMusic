package com.team28.wondermusic.ui.forgetpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.data.models.ForgetEmailModal
import com.team28.wondermusic.databinding.FragmentStepEmailBinding

class StepEmailFragment : Fragment() {

    private lateinit var binding: FragmentStepEmailBinding
    private val viewModel by viewModels<ForgetPasswordViewModel>({ requireActivity() })

    private var isEmailValid = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStepEmailBinding.inflate(inflater, container, false)

        binding.btnForgetPass.setOnClickListener {
            if (isEmailValid) {
                viewModel.email = binding.edEmail.text.toString().trim()
                viewModel.forgetEmail(ForgetEmailModal(viewModel.email))
            }
        }

        viewModel.loadingEmail.observe(viewLifecycleOwner) {
            binding.btnForgetPass.isEnabled = !it
        }

        viewModel.emailStatus.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    viewModel.actionNextStep.postValue(true)
                } else {
                    binding.tvEmailError.text = viewModel.message
                    binding.tvEmailError.visibility = View.VISIBLE

                    Toast.makeText(context, viewModel.message, Toast.LENGTH_SHORT).show()
                }
                viewModel.emailStatus.value = null
            }
        }

        binding.edEmail.addTextChangedListener {
            it?.let {
                isEmailValid = Helper.validateEmail(it.toString().trim())
                binding.tvEmailError.visibility = if (isEmailValid) View.GONE else View.VISIBLE
            }
        }

        return binding.root
    }

}