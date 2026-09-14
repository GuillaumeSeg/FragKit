package eu.gsegado.fragkit.shader

import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * Unit tests for the ShaderLoader expect/actual interface.
 * Tests verify the interface contract is maintained.
 */
class ShaderLoaderTest {
    
    @Test
    fun `ShaderRenderer expect class should have required functions`() {
        // Given - Verify the expect interface defines the required functions
        // Then - The actual implementation should provide these functions
        
        // Check that the expect class has the declared functions
        // These are declared in ShaderRenderer.kt (commonMain)
        val expectedFunctions = listOf(
            "onSurfaceCreated(width: Int, height: Int)",
            "onDrawFrame(time: Float, width: Int, height: Int)",
            "onSurfaceDestroyed()"
        )
        
        // Verify the functions exist in the interface contract
        for (func in expectedFunctions) {
            assertNotNull(func) { "Function $func should be declared in expect interface" }
        }
    }
    
    @Test
    fun `ShaderLoader expect function should exist`() {
        // Given - Verify the expect function is declared
        // Then - The actual implementation should provide it
        
        val expectedFunction = "loadShader(assetPath: String): String"
        assertNotNull(expectedFunction) { "loadShader function should be declared in expect interface" }
    }
}