package eu.gsegado.fragkit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import eu.gsegado.fragkit.shader.ShaderRenderer
import eu.gsegado.fragkit.ui.HomeScreen
import eu.gsegado.fragkit.ui.ShaderScreen
import eu.gsegado.fragkit.shader.loadShader

@Composable
fun App() {
    val currentScreen = remember { mutableStateOf<Screen>(Screen.Home) }
    val shaderSource = remember { mutableStateOf<String?>(null) }
    
    when (currentScreen.value) {
        Screen.Home -> {
            HomeScreen(
                onLoadShaderClick = {
                    // Load the default shader for demo purposes
                    shaderSource.value = loadShader("plasma.frag")
                    currentScreen.value = Screen.Shader
                }
            )
        }
        
        Screen.Shader -> {
            shaderSource.value?.let { shaderSrc ->
                val renderer = remember { ShaderRenderer(shaderSrc) }
                ShaderScreen(
                    shaderRenderer = renderer,
                    onBackPressed = { currentScreen.value = Screen.Home }
                )
            }
        }
    }
}

sealed interface Screen {
    object Home : Screen
    object Shader : Screen
}
