package com.vinh.wondermusic.data.services.account

import com.vinh.wondermusic.base.network.BaseRemoteService
import com.vinh.wondermusic.base.network.NetworkResult
import com.vinh.wondermusic.data.apis.AccountAPI
import com.vinh.wondermusic.data.models.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class AccountRemoteService @Inject constructor(
    private val accountAPI: AccountAPI
) : BaseRemoteService() {

    suspend fun login(modal: LoginModal): NetworkResult<LoginResponseJson> {
        return callApi { accountAPI.login(modal) }
    }

    suspend fun signup(modal: SignupModal): NetworkResult<MessageJson> {
        return callApi { accountAPI.signup(modal) }
    }

    suspend fun sendAccountDevice(deviceToken: String): NetworkResult<MessageJson> {
        return callApi { accountAPI.sendAccountDevice(DeviceTokenModal(deviceToken)) }
    }

    suspend fun forgetEmail(modal: ForgetEmailModal): NetworkResult<MessageJson> {
        return callApi { accountAPI.forgetEmail(modal) }
    }

    suspend fun forgetCode(modal: ForgetCodeModal): NetworkResult<MessageJson> {
        return callApi { accountAPI.forgetCode(modal) }
    }

    suspend fun forgetChangePassword(modal: ForgetChangeModal): NetworkResult<MessageJson> {
        return callApi { accountAPI.forgetChangePassword(modal) }
    }

    suspend fun updateAccount(account: AccountUpdate): NetworkResult<MessageJson> {
        return callApi {
            if (account.avatar != null) {
                val imageFileRequestBody =
                    account.avatar.asRequestBody("image/*".toMediaTypeOrNull())
                accountAPI.updateAccount(
                    name = account.name.toRequestBody(MultipartBody.FORM),
                    img = MultipartBody.Part.createFormData(
                        "image",
                        account.avatar.name,
                        imageFileRequestBody
                    )
                )
            } else {
                accountAPI.updateAccount(
                    idAccount = account.idAccount,
                    name = account.name.toRequestBody(MultipartBody.FORM),
                )
            }
        }

    }

    suspend fun followAccount(idAccount: Int): NetworkResult<MessageJson> {
        return callApi { accountAPI.followAccount(idAccount) }
    }

    suspend fun unFollowAccount(idAccount: Int): NetworkResult<MessageJson> {
        return callApi { accountAPI.unFollowAccount(idAccount) }
    }

    suspend fun getAccount(idAccount: Int): Account? {
        val result = callApi { accountAPI.getAccount(idAccount) }
        if (result is NetworkResult.Success) {
            return result.body.data.toAccount()
        }
        return null
    }

    suspend fun searchAccount(keyword: String): List<Account> {
        val result = callApi { accountAPI.searchAccount(keyword) }
        return if (result is NetworkResult.Success) {
            result.body.data.toListAccount()
        } else {
            emptyList()
        }
    }

    suspend fun getSongsOfAccount(idAccount: Int): List<Song> {
        val result = callApi { accountAPI.getSongsOfAccount(idAccount) }
        return if (result is NetworkResult.Success) {
            result.body.data.toListSong()
        } else {
            emptyList()
        }
    }

    suspend fun getMySongs(): List<Song> {
        val result = callApi { accountAPI.getMySongs() }
        return if (result is NetworkResult.Success) {
            result.body.data.toListSong()
        } else {
            emptyList()
        }
    }

    suspend fun getFollowers(idAccount: Int): List<Account> {
        val result = callApi { accountAPI.getFollowers(idAccount) }
        return if (result is NetworkResult.Success) {
            result.body.data.toListAccount()
        } else {
            emptyList()
        }
    }

    suspend fun getFollowings(idAccount: Int): List<Account> {
        val result = callApi { accountAPI.getFollowings(idAccount) }
        return if (result is NetworkResult.Success) {
            result.body.data.toListAccount()
        } else {
            emptyList()
        }
    }

    suspend fun getTopAccounts(): List<Account> {
        val result = callApi { accountAPI.getTopAccounts() }
        return if (result is NetworkResult.Success) {
            result.body.data.toListAccount()
        } else {
            emptyList()
        }
    }

    suspend fun getAlbumsOfAccount(idAccount: Int): List<Album> {
        val result = callApi { accountAPI.getAlbumsOfAccount(idAccount) }
        return if (result is NetworkResult.Success) {
            result.body.data.toListAlbum()
        } else {
            emptyList()
        }
    }

    suspend fun getPlaylistsOfAccount(idAccount: Int): List<Playlist> {
        val result = callApi { accountAPI.getPlaylistsOfAccount(idAccount) }
        return if (result is NetworkResult.Success) {
            result.body.data.toListPlaylist()
        } else {
            emptyList()
        }
    }

    suspend fun changePassword(modal: ChangePasswordModal): NetworkResult<MessageJson> {
        return callApi { accountAPI.changePassword(modal) }
    }

    suspend fun getLockAccounts(): List<Account> {
        val result = callApi { accountAPI.getLockAccounts() }
        return if (result is NetworkResult.Success) {
            result.body.data.toListAccount()
        } else {
            emptyList()
        }
    }

    suspend fun lockAccount(idAccount: Int): NetworkResult<MessageJson> {
        return callApi { accountAPI.lockAccount(idAccount) }
    }

    suspend fun unlockAccount(idAccount: Int): NetworkResult<MessageJson> {
        return callApi { accountAPI.unlockAccount(idAccount) }
    }
}