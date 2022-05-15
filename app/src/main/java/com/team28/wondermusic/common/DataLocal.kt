package com.team28.wondermusic.common

import com.team28.wondermusic.data.TempData
import com.team28.wondermusic.data.models.Account


object DataLocal {
    var ACCESS_TOKEN =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZF9hY2NvdW50IjoiMSIsImlhdCI6MTY1MTUwMzkwNSwiZXhwIjoxNjUyMzY3OTA1fQ.I7xwAnOBSdaDCCHOTubWPZ3rBnKBDiDvC6acsLiq8v8"
    var IS_LOGGED_IN = true
    var IS_SHUFFLE = false
    var IS_REPEAT = false

    var myAccount = TempData.accounts[0]
}