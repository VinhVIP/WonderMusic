package com.team28.wondermusic.ui.account.changeprofile

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.squareup.picasso.Picasso
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.common.FileUtils
import com.team28.wondermusic.data.models.Account
import com.team28.wondermusic.databinding.FragmentChangeProfileBinding
import java.io.File


class ChangeProfileFragment : DialogFragment() {

    private lateinit var binding: FragmentChangeProfileBinding

    private var account: Account? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeProfileBinding.inflate(inflater, container, false)

        account = arguments?.getParcelable(Constants.Account)

        account?.let {
            Picasso.get().load(it.avatar).fit().into(binding.imgAvatar)
            binding.edAccountName.setText(it.accountName)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPickImage.setOnClickListener {
            try {
                fileChooser.launch("image/*")
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    requireContext(),
                    "Vui lòng cài đặt File Manager",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private var fileChooser: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        val file: File? = FileUtils.from(requireContext(), uri!!)
        file?.let {
            Picasso.get().load(it).resize(200, 200).into(binding.imgAvatar)
        }
    }
}