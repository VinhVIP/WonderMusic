package com.team28.wondermusic.ui.menubottom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.team28.wondermusic.R
import com.team28.wondermusic.adapter.MenuBottomAdapter
import com.team28.wondermusic.base.fragments.BaseDialogFragment
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.data.models.*
import com.team28.wondermusic.databinding.FragmentMenuBottomBinding
import com.team28.wondermusic.ui.formsong.FormSongActivity
import com.team28.wondermusic.ui.player.singers.SingersFragment

class MenuBottomFragment : BaseDialogFragment(), MenuBottomClickListener {

    private lateinit var binding: FragmentMenuBottomBinding
    private lateinit var menuAdapter: MenuBottomAdapter

    private var song: Song? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        song = arguments?.getParcelable(Constants.Song)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuAdapter = MenuBottomAdapter(this)
        menuAdapter.differ.submitList(generateMenu())

        binding.recyclerMenu.apply {
            adapter = menuAdapter
            layoutManager = LinearLayoutManager(this@MenuBottomFragment.context)
        }

        song?.let { song ->
            Picasso.get().load(song.image).fit().into(binding.imgSongAvatar)
            binding.tvSongName.text = song.name
            binding.tvAccountName.text = song.account?.accountName ?: ""
        }

    }

    private fun generateMenu(): ArrayList<MenuBottom> {
        val items = arrayListOf<MenuBottom>()

        val myAccount =
            Account(1, "vinhvipit@gmail.com", "Quang Vinh", "", "01/01/2020", 0, 0, 123, 45)


        song?.let { song ->
            if (myAccount.idAccount == song.account!!.idAccount) {
                items.add(MenuBottom("Chỉnh sửa", R.drawable.ic_edit, MenuBottomType.EDIT))
            }

            if (song.loveStatus)
                items.add(
                    MenuBottom(
                        "Xóa khỏi yêu thích",
                        R.drawable.ic_heart_red,
                        MenuBottomType.REMOVE_FAVORITE
                    )
                )
            else
                items.add(
                    MenuBottom(
                        "Thêm vào yêu thích",
                        R.drawable.ic_heart,
                        MenuBottomType.FAVORITE
                    )
                )

            items.addAll(coreMenu)
        }

        return items
    }

    private var coreMenu = arrayListOf(
        MenuBottom("Thêm vào playlist", R.drawable.ic_playlist_add, MenuBottomType.PLAYLIST),
        MenuBottom("Phát kế tiếp", R.drawable.ic_play, MenuBottomType.HEAD_OF_PLAYLIST),
        MenuBottom(
            "Thêm vào danh sách phát",
            R.drawable.ic_playlist_current,
            MenuBottomType.TAIL_OF_PLAYLIST
        ),
        MenuBottom("Xem album", R.drawable.ic_album, MenuBottomType.ALBUM),
        MenuBottom("Xem nghệ sĩ", R.drawable.ic_singers, MenuBottomType.SINGERS),
        MenuBottom("Tải về", R.drawable.ic_download, MenuBottomType.DOWNLOAD),
    )

    override fun onMenuClick(menu: MenuBottom) {
        Toast.makeText(context, menu.title, Toast.LENGTH_SHORT).show();
        when (menu.type) {
            MenuBottomType.EDIT -> {
                startActivity(Intent(context, FormSongActivity::class.java).apply {
                    putExtra(Constants.Song, song)
                })
            }
            MenuBottomType.SINGERS -> {
                SingersFragment().apply {
                    arguments = Bundle().apply {
                        Log.d("vinh", "singers size: ${song?.singers?.size}")
                        putParcelableArrayList(
                            Constants.Singers,
                            song?.singers as ArrayList
                        )
                    }
                }.show(requireActivity().supportFragmentManager, null)
            }
        }

        dismiss()
    }

}