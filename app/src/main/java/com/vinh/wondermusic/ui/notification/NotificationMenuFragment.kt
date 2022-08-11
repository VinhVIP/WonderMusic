package com.vinh.wondermusic.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.vinh.wondermusic.base.fragments.BaseDialogFragment
import com.vinh.wondermusic.common.Constants
import com.vinh.wondermusic.data.models.Notification
import com.vinh.wondermusic.databinding.FragmentNotificationMenuBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationMenuFragment : BaseDialogFragment() {

    private lateinit var binding: FragmentNotificationMenuBinding
    private val viewModel by viewModels<NotificationViewModel>({ requireActivity() })

    private var notification: Notification? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notification = arguments?.getParcelable(Constants.Notification)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationMenuBinding.inflate(inflater, container, false)

        binding.menuReadNotification.setOnClickListener {
            notification?.let {
                viewModel.readNotification(it)
                this.dismiss()
            }
        }

        binding.menuDeleteNotification.setOnClickListener {
            notification?.let {
                viewModel.deleteNotification(it)
                this.dismiss()
            }
        }

        return binding.root
    }

}