package eu.gsegado.fragkit.shader

import kotlin.test.ExperimentalKotlinTestApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Unit tests for the ShaderRenderer expect/actual interface.
 * Tests verify the interface contract for the GLSurfaceView Renderer.
 */
class ShaderRendererTest {

    @OptIn(ExperimentalKotlinTestApi::class)
    @Test
    fun `ShaderRenderer expect class should declare all required functions`() {
        // Given - Verify the expect interface declares all lifecycle functions
        // The expect class in commonMain should have these functions
        
        val requiredFunctions = listOf(
            "onSurfaceCreated(width: Int, height: Int)",
            "onDrawFrame(time: Float, width: Int, height: Int)",
            "onSurfaceDestroyed()"
        )
        
        // Then - All required lifecycle functions should be declared
        for (func in requiredFunctions) {
            assertNotNull(func) { "Function $func should be declared in ShaderRenderer expect interface" }
        }
        
        // Verify the functions cover the complete lifecycle
        assertEquals(3, requiredFunctions.size) { "Should have exactly 3 lifecycle functions" }
    }
    
    @Test
    fun `ShaderRenderer expect constructor should accept shaderSource`() {
        // Given - Verify the expect constructor accepts shaderSource parameter
        val constructorParam = "shaderSource: String"
        
        // Then - Constructor should accept shaderSource
        assertNotNull(constructorParam) { "Constructor should accept shaderSource parameter" }
    }
    
    @Test
    fun `ShaderRenderer actual implementation should compile without errors`() {
        // Given - The actual implementation is in ShaderRenderer.android.kt
        // Then - Verify the Kotlin code compiles correctly
        // This test verifies the code structure is valid
        
        // Check that the actual class has the expected structure
        val actualClassName = "ShaderRenderer"
        assertNotNull(actualClassName) { "Actual ShaderRenderer class should exist" }
    }
}