package com.appsheaven.keepup

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KeepUp",
    ) {
        App()
    }
}