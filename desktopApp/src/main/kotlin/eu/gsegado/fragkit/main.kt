package eu.gsegado.fragkit

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "FragKit",
    ) {
        App()
    }
}