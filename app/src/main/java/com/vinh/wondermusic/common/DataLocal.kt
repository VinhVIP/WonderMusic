package com.vinh.wondermusic.common

import com.vinh.wondermusic.data.TempData


object DataLocal {
    var ACCESS_TOKEN =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZF9hY2NvdW50IjoiMSIsImlhdCI6MTY1MTUwMzkwNSwiZXhwIjoxNjUyMzY3OTA1fQ.I7xwAnOBSdaDCCHOTubWPZ3rBnKBDiDvC6acsLiq8v8"
    var IS_LOGGED_IN = true
    var IS_SHUFFLE = false
    var IS_REPEAT = false

    var myAccount = TempData.accounts[0]

    var externalFileFir = ""
}