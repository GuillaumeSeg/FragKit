package eu.gsegado.fragkit.shader

import android.content.Context
import android.util.Log

// This would typically be set by the Android application
// For simplicity, we're using a global variable
private var applicationContext: Context? = null

actual fun loadShader(assetPath: String): String {
    val context = applicationContext
        ?: throw IllegalStateException("Application context not set. Call initializeApplicationContext() first.")
    
    Log.d("ShaderLoader", "Loading shader from: $assetPath")
    val shaderText = context.assets.open(assetPath).bufferedReader().use { it.readText() }
    Log.d("ShaderLoader", "Loaded shader length: ${shaderText.length} characters")
    return shaderText
}

fun initializeApplicationContext(context: Context) {
    applicationContext = context.applicationContext
}