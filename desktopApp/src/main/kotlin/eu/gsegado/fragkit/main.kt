package eu.gsegado.fragkit

import eu.gsegado.fragkit.shader.loadShader
import eu.gsegado.fragkit.shader.ShaderRenderer
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryUtil

fun main() {
    val shaderSource = loadShader("shaders/plasma.frag")
    val renderer = ShaderRenderer(shaderSource)

    GLFWErrorCallback.createPrint().apply { set() }

    if (!glfwInit()) {
        throw IllegalStateException("Unable to initialize GLFW")
    }

    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)

    val windowWidth = 1280
    val windowHeight = 720
    val window = glfwCreateWindow(windowWidth, windowHeight, "FragKit - Shader Preview", MemoryUtil.NULL, MemoryUtil.NULL)
    if (window == MemoryUtil.NULL) {
        throw IllegalStateException("Failed to create GLFW window")
    }

    glfwMakeContextCurrent(window)
    GL.createCapabilities()

    renderer.onSurfaceCreated(windowWidth, windowHeight)

    renderer.runLoop()

    renderer.onSurfaceDestroyed()
    glfwDestroyWindow(window)
    glfwTerminate()
}