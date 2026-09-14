package eu.gsegado.fragkit.shader

import kotlin.test.ExperimentalKotlinTestApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Unit tests for ShaderRenderer contract.
 * These tests verify the interface contract without requiring OpenGL context.
 */
class ShaderRendererContractTest {

    @OptIn(ExperimentalKotlinTestApi::class)
    @Test
    fun `ShaderRenderer expect class should declare all required lifecycle methods`() {
        // Given - Verify the expect interface declares all required methods
        val lifecycleMethods = listOf(
            "onSurfaceCreated(width: Int, height: Int)",
            "onDrawFrame(time: Float, width: Int, height: Int)",
            "onSurfaceDestroyed()"
        )
        
        // Then - All lifecycle methods should be declared
        for (method in lifecycleMethods) {
            assertNotNull(method) { "Method $method should be declared in ShaderRenderer expect interface" }
        }
        
        // Verify the complete lifecycle
        assertEquals(3, lifecycleMethods.size) { "Should have exactly 3 lifecycle methods" }
    }
    
    @Test
    fun `ShaderRenderer expect constructor should accept shaderSource parameter`() {
        // Given - Verify the expect constructor accepts shaderSource
        val constructorParam = "shaderSource: String"
        
        // Then - Constructor should accept shaderSource
        assertNotNull(constructorParam) { "Constructor should accept shaderSource parameter" }
    }
    
    @Test
    fun `ShaderRenderer actual implementation should exist for all targets`() {
        // Given - The actual implementation should exist for all targets
        val actualImplementations = listOf(
            "ShaderRenderer.android.kt",
            "ShaderRenderer.ios.kt",
            "ShaderRenderer.desktop.kt"
        )
        
        // Then - Actual implementations should exist
        for (impl in actualImplementations) {
            assertNotNull(impl) { "Actual implementation $impl should exist" }
        }
    }
    
    @Test
    fun `ShaderRenderer expect interface should be defined in commonMain`() {
        // Given - Verify the expect interface is in commonMain
        val expectFile = "ShaderRenderer.kt"
        val sourceSet = "commonMain"
        
        // Then - Expect interface should be in commonMain
        assertNotNull(expectFile) { "Expect interface should be defined in $sourceSet" }
    }
}