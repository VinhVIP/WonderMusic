package com.team28.wondermusic.ui.menubottom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.team28.wondermusic.R
import com.team28.wondermusic.adapter.MenuBottomAdapter
import com.team28.wondermusic.base.fragments.BaseDialogFragment
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.data.TempData
import com.team28.wondermusic.data.models.MenuBottom
import com.team28.wondermusic.data.models.MenuBottomClickListener
import com.team28.wondermusic.data.models.MenuBottomType.*
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.FragmentMenuBottomBinding
import com.team28.wondermusic.ui.account.album_detail.AlbumDetailFragment
import com.team28.wondermusic.ui.formsong.FormSongActivity
import com.team28.wondermusic.ui.player.singers.SingersFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuBottomFragment : BaseDialogFragment(), MenuBottomClickListener {

    private lateinit var binding: FragmentMenuBottomBinding
    private val viewModel by viewModels<MenuBottomViewModel>({ requireActivity() })
    private lateinit var menuAdapter: MenuBottomAdapter

    private lateinit var song: Song

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val song: Song? = arguments?.getParcelable(Constants.Song)
        if (song == null) dismiss()
        else this.song = song

        viewModel.position = arguments?.getInt(Constants.Position)
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

        if (song.image.isNotEmpty()) Picasso.get().load(song.image).fit()
            .into(binding.imgSongAvatar)
        binding.tvSongName.text = song.name
        binding.tvAccountName.text = song.account?.accountName ?: ""

    }

    private fun generateMenu(): ArrayList<MenuBottom> {
        val items = arrayListOf<MenuBottom>()

        val myAccount = TempData.myAccount

        if (myAccount.idAccount == song.account!!.idAccount) {
            items.add(MenuBottom("Chỉnh sửa", R.drawable.ic_edit, EDIT))
        }

        if (song.loveStatus)
            items.add(
                MenuBottom(
                    "Xóa khỏi yêu thích",
                    R.drawable.ic_heart_red,
                    REMOVE_FAVORITE
                )
            )
        else
            items.add(
                MenuBottom(
                    "Thêm vào yêu thích",
                    R.drawable.ic_heart,
                    FAVORITE
                )
            )

        items.addAll(coreMenu)

        return items
    }

    private var coreMenu = arrayListOf(
        MenuBottom("Thêm vào playlist", R.drawable.ic_playlist_add, PLAYLIST),
        MenuBottom("Phát kế tiếp", R.drawable.ic_play, HEAD_OF_PLAYLIST),
        MenuBottom(
            "Thêm vào danh sách phát",
            R.drawable.ic_playlist_current,
            TAIL_OF_PLAYLIST
        ),
        MenuBottom("Xem album", R.drawable.ic_album, ALBUM),
        MenuBottom("Xem nghệ sĩ", R.drawable.ic_singers, SINGERS),
        MenuBottom("Tải về", R.drawable.ic_download, DOWNLOAD),
    )

    override fun onMenuClick(menu: MenuBottom) {
        when (menu.type) {
            EDIT -> {
                startActivity(Intent(context, FormSongActivity::class.java).apply {
                    putExtra(Constants.Song, song)
                })
            }
            FAVORITE -> {
                viewModel.loveSong(song)
            }
            REMOVE_FAVORITE -> {
                viewModel.unLoveSong(song)
            }
            ALBUM -> {
                if (song.album != null) {
                    AlbumDetailFragment().apply {
                        arguments = Bundle().apply {
                            putParcelable(Constants.Album, song.album)
                            putBoolean(Constants.NeedReload, true)
                        }
                    }.show(requireActivity().supportFragmentManager, null)
                } else {
                    Toast.makeText(context, "Bài hát nào không thuộc album nào", Toast.LENGTH_SHORT)
                        .show()
                }

            }
            SINGERS -> {
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