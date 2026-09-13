package eu.gsegado.fragkit.ui

import android.opengl.GLSurfaceView
import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import eu.gsegado.fragkit.shader.ShaderRenderer

@Composable
actual fun ShaderScreen(
    shaderRenderer: ShaderRenderer,
    onBackPressed: () -> Unit
) {
    AndroidView(
        factory = { context ->
            GLSurfaceView(context).apply {
                setEGLContextClientVersion(3)
                setRenderer(shaderRenderer)
                renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
            }
        },
        modifier = Modifier.fillMaxSize()
    )
    
    // Call onBackPressed when the user presses the back button
    DisposableEffect(Unit) {
        onDispose {
            shaderRenderer.onSurfaceDestroyed()
        }
    }
}