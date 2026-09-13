package eu.gsegado.fragkit.shader

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.util.Log
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

actual class ShaderRenderer actual constructor(private val shaderSource: String) : GLSurfaceView.Renderer {
    private var program = 0
    private var startTime = 0L
    private var width = 0
    private var height = 0
    
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        
        // Load vertex shader from file
        Log.d("ShaderRenderer", "Loading vertex shader from shaders/vertex.glsl")
        val vertexShaderSource = loadShader("shaders/vertex.glsl")
        Log.d("ShaderRenderer", "Vertex shader source:\n$vertexShaderSource")
        val vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderSource)
        
        Log.d("ShaderRenderer", "Loading fragment shader")
        val fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, shaderSource)
        
        // Create program
        program = GLES30.glCreateProgram()
        GLES30.glAttachShader(program, vertexShader)
        Log.d("ShaderRenderer", "Attaching vertex shader: $vertexShader")
        GLES30.glAttachShader(program, fragmentShader)
        Log.d("ShaderRenderer", "Attaching fragment shader: $fragmentShader")
        GLES30.glLinkProgram(program)
        
        // Check linking status
        val linkStatus = IntArray(1)
        GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] != GLES30.GL_TRUE) {
            val linkLog = GLES30.glGetProgramInfoLog(program)
            Log.e("ShaderRenderer", "Could not link program: $linkLog")
            GLES30.glDeleteProgram(program)
            program = 0
            return
        } else {
            Log.d("ShaderRenderer", "Successfully linked program: $program")
        }
        
        startTime = System.nanoTime()
    }
    
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        this.width = width
        this.height = height
        GLES30.glViewport(0, 0, width, height)
    }
    
    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        
        if (program == 0) return
        
        GLES30.glUseProgram(program)
        
        // Set time uniform
        val timeLocation = GLES30.glGetUniformLocation(program, "u_time")
        if (timeLocation >= 0) {
            val time = (System.nanoTime() - startTime) / 1_000_000_000.0f
            GLES30.glUniform1f(timeLocation, time)
        }
        
        // Set resolution uniform
        val resolutionLocation = GLES30.glGetUniformLocation(program, "u_resolution")
        if (resolutionLocation >= 0) {
            GLES30.glUniform2f(resolutionLocation, width.toFloat(), height.toFloat())
        }
        
        // Draw fullscreen quad
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4)
    }
    
    actual fun onSurfaceCreated(width: Int, height: Int) {
        // This will be handled by the GLSurfaceView callbacks
    }
    
    actual fun onDrawFrame(time: Float, width: Int, height: Int) {
        // This will be handled by the GLSurfaceView callbacks
    }
    
    actual fun onSurfaceDestroyed() {
        if (program != 0) {
            GLES30.glDeleteProgram(program)
            program = 0
        }
    }
    
    private fun loadShader(type: Int, shaderSource: String): Int {
        val shaderName = when (type) {
            GLES30.GL_VERTEX_SHADER -> "Vertex"
            GLES30.GL_FRAGMENT_SHADER -> "Fragment"
            else -> "Unknown"
        }
        Log.d("ShaderRenderer", "Compiling $shaderName shader")
        Log.d("ShaderRenderer", "Shader source:\n$shaderSource")
        
        val shader = GLES30.glCreateShader(type)
        GLES30.glShaderSource(shader, shaderSource)
        GLES30.glCompileShader(shader)
        
        // Check compilation status
        val compileStatus = IntArray(1)
        GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compileStatus, 0)
        if (compileStatus[0] != GLES30.GL_TRUE) {
            val log = GLES30.glGetShaderInfoLog(shader)
            Log.e("ShaderRenderer", "Error compiling $shaderName shader: $log")
            GLES30.glDeleteShader(shader)
            return 0
        } else {
            Log.d("ShaderRenderer", "Successfully compiled $shaderName shader")
        }
        
        return shader
    }
}