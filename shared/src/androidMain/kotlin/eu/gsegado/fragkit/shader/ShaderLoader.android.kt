package eu.gsegado.fragkit.shader

import android.content.Context
import android.util.Log

// This would typically be set by the Android application
// For simplicity, we're using a global variable
private var appContext: Context? = null

actual fun loadShader(assetPath: String): String {
    val context = appContext
        ?: throw IllegalStateException("The Android Context is not initialized. Call initializeApplicationContext() during the App Launch.")

    try {
        // Normalize asset path for commonMain resources
        val normalizedPath = if (assetPath.startsWith("shaders/")) {
            assetPath
        } else {
            "shaders/$assetPath"
        }
        
        Log.d("ShaderLoader", "Loading from : $normalizedPath")
        val stream = context.assets.open(normalizedPath)
        val shaderText = stream.bufferedReader().use { it.readText() }
        Log.d("ShaderLoader", "Shader Loaded : ${shaderText.length} caracters")
        return shaderText
    } catch (e: Exception) {
        val normalizedPath = if (assetPath.startsWith("shaders/")) {
            assetPath
        } else {
            "shaders/$assetPath"
        }
        Log.e("ShaderLoader", "Loading error : $normalizedPath", e)
        throw e
    }
}

fun initializeApplicationContext(context: Context) {
    appContext = context.applicationContext
}