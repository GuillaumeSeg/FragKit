package eu.gsegado.fragkit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import eu.gsegado.fragkit.shader.ShaderRenderer
import eu.gsegado.fragkit.shader.loadShader
import eu.gsegado.fragkit.theme.FragKitTheme
import eu.gsegado.fragkit.ui.HomeScreen
import eu.gsegado.fragkit.ui.ShaderScreen

@Composable
fun FragKitApp() {
    FragKitTheme {
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
        var shaderRenderer by remember { mutableStateOf<ShaderRenderer?>(null) }
        
        when (currentScreen) {
            is Screen.Home -> {
                HomeScreen(
                    onLoadShaderClick = {
                        // Load the shader and navigate to shader screen
                        try {
                            val shaderSource = loadShader("shaders/plasma.frag")
                            shaderRenderer = ShaderRenderer(shaderSource)
                            currentScreen = Screen.Shader
                        } catch (e: Exception) {
                            // Handle error
                            e.printStackTrace()
                        }
                    }
                )
            }
            is Screen.Shader -> {
                shaderRenderer?.let { renderer ->
                    ShaderScreen(
                        shaderRenderer = renderer,
                        onBackPressed = { currentScreen = Screen.Home }
                    )
                }
            }
        }
    }
}

sealed class Screen {
    object Home : Screen()
    object Shader : Screen()
}