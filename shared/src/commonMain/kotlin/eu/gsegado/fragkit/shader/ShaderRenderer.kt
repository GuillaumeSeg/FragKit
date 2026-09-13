package eu.gsegado.fragkit.shader

expect class ShaderRenderer(shaderSource: String) {
    fun onSurfaceCreated(width: Int, height: Int)
    fun onDrawFrame(time: Float, width: Int, height: Int)
    fun onSurfaceDestroyed()
}