package eu.gsegado.fragkit.shader

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer

actual class ShaderRenderer actual constructor(private val shaderSource: String) {
    private var window: Long = MemoryUtil.NULL
    private var program = 0
    private var startTime = 0L
    private var width = 0
    private var height = 0
    private var vao = 0
    private var isRunning = false

    init {
        if (!glfwInit()) {
            throw IllegalStateException("Failed to initialize GLFW")
        }
        val errorCallback = GLFWErrorCallback.createPrint()
        glfwSetErrorCallback(errorCallback)
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
    }

    fun createWindow(title: String, width: Int, height: Int) {
        this.width = width
        this.height = height
        window = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)
        if (window == MemoryUtil.NULL) {
            throw IllegalStateException("Failed to create GLFW window")
        }
        glfwMakeContextCurrent(window)
        GL.createCapabilities()
        startTime = System.nanoTime()
        isRunning = true
    }

    fun runLoop() {
        while (isRunning && !glfwWindowShouldClose(window)) {
            val time = (System.nanoTime() - startTime) / 1_000_000_000.0f
            onDrawFrame(time, width, height)
            glfwSwapBuffers(window)
            glfwPollEvents()
            if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
                isRunning = false
            }
        }
    }

    actual fun onSurfaceCreated(width: Int, height: Int) {
        setupShaderProgram()
        glViewport(0, 0, width, height)
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    }

    actual fun onDrawFrame(time: Float, width: Int, height: Int) {
        glClear(GL_COLOR_BUFFER_BIT)
        if (program == 0) return
        glUseProgram(program)
        val timeLocation = glGetUniformLocation(program, "u_time")
        if (timeLocation >= 0) {
            glUniform1f(timeLocation, time)
        }
        val resolutionLocation = glGetUniformLocation(program, "u_resolution")
        if (resolutionLocation >= 0) {
            glUniform2f(resolutionLocation, width.toFloat(), height.toFloat())
        }
        glBindVertexArray(vao)
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)
    }

    actual fun onSurfaceDestroyed() {
        cleanup()
    }

    private fun setupShaderProgram() {
        val vertexShaderSource = loadShader("shaders/vertex.glsl")
        val vertexShaderObj = compileShader(GL_VERTEX_SHADER, vertexShaderSource)
        val fragmentShaderObj = compileShader(GL_FRAGMENT_SHADER, shaderSource)
        program = glCreateProgram()
        glAttachShader(program, vertexShaderObj)
        glAttachShader(program, fragmentShaderObj)
        glLinkProgram(program)
        val linkStatus = IntBuffer.allocate(1)
        glGetProgramiv(program, GL_LINK_STATUS, linkStatus)
        if (linkStatus[0] != GL_TRUE) {
            val linkLog = glGetProgramInfoLog(program)
            glDeleteProgram(program)
            program = 0
            throw IllegalStateException("Failed to link shader program: $linkLog")
        }
        glDeleteShader(vertexShaderObj)
        glDeleteShader(fragmentShaderObj)
        setupFullscreenQuad()
    }

    private fun compileShader(type: Int, source: String): Int {
        val shader = glCreateShader(type)
        glShaderSource(shader, source)
        glCompileShader(shader)
        val compileStatus = IntBuffer.allocate(1)
        glGetShaderiv(shader, GL_COMPILE_STATUS, compileStatus)
        if (compileStatus[0] != GL_TRUE) {
            val log = glGetShaderInfoLog(shader)
            glDeleteShader(shader)
            throw IllegalStateException("Failed to compile shader: $log")
        }
        return shader
    }

    private fun setupFullscreenQuad() {
        val vaoBuf = IntBuffer.allocate(1)
        glGenVertexArrays(vaoBuf)
        vao = vaoBuf[0]
        glBindVertexArray(vao)
        val vertices = floatArrayOf(
            -1f, -1f,
             1f, -1f,
            -1f,  1f,
             1f,  1f
        )
        val vboBuf = IntBuffer.allocate(1)
        glGenBuffers(vboBuf)
        val vbo = vboBuf[0]
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        val verticesBuffer = FloatBuffer.allocate(vertices.size)
        verticesBuffer.put(vertices).flip()
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW)
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0L)
        glEnableVertexAttribArray(0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)
    }

    private fun cleanup() {
        if (program != 0) {
            glDeleteProgram(program)
            program = 0
        }
        if (window != MemoryUtil.NULL) {
            glfwDestroyWindow(window)
            window = MemoryUtil.NULL
        }
        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }
}