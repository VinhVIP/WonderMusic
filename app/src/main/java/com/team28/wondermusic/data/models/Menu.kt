package com.team28.wondermusic.data.models

enum class MenuIndividualType { FOLLOWER, FOLLOWING, SONG, FAVORITE, PLAYLIST, ALBUM }

data class MenuIndividual(val title: String, val drawableRes: Int, val type: MenuIndividualType)

interface MenuClickListener {
    fun onMenuClick(menu: MenuIndividual)
}

enum class MenuBottomType { EDIT, FAVORITE, REMOVE_FAVORITE, PLAYLIST, HEAD_OF_PLAYLIST, TAIL_OF_PLAYLIST, ALBUM, SINGERS, DOWNLOAD }

data class MenuBottom(val title: String, val drawableRes: Int, val type: MenuBottomType)

interface MenuBottomClickListener {
    fun onMenuClick(menu: MenuBottom)
}