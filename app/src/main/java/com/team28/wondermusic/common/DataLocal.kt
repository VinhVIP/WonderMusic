package com.team28.wondermusic.common

import com.team28.wondermusic.data.models.Account


object DataLocal {
    var ACCESS_TOKEN =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZF9hY2NvdW50IjoiMSIsImlhdCI6MTY1MDc3MzA5NSwiZXhwIjoxNjUxNjM3MDk1fQ.7b4fJx8S_dAyEG-A3YkFxVCMJ0gR03vRVcr8W7eyNz0"
    var IS_LOGGED_IN = true

    var myAccount = Account(idAccount = 1, accountName = "Vinh VIP")
}