package eu.gsegado.fragkit.shader

/**
 * Charge un fichier shader depuis les ressources du classpath.
 * Fallback sur le contexte actuel du classloader pour supporter Gradle et les JARs.
 */
actual fun loadShader(assetPath: String): String {
    // Essai 1 : Context ClassLoader (souvent utilisé par Gradle)
    val stream1 = Thread.currentThread().contextClassLoader
        ?.getResourceAsStream(assetPath)

    // Essai 2 : ClassLoader de l'objet anonyme (fallback ultime)
    val stream2 = object {}.javaClass.classLoader?.getResourceAsStream(assetPath)

    val stream = stream1 ?: stream2

    return stream?.bufferedReader()?.use { it.readText() }
        ?: throw IllegalArgumentException("Shader introuvable pour le chemin : '$assetPath'")
}