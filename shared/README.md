# Shared Module

The shared module contains the core of the FragKit application, including shader rendering logic and Compose Multiplatform UI used by all platforms (Android, iOS, Desktop).

## Overview

The shared module provides:
- **UI Components**: HomeScreen, ShaderScreen (Compose Multiplatform)
- **Shader Handling**: ShaderRenderer (expect/actual), ShaderLoader (expect/actual)
- **Theme**: Dark, minimal theme implementation
- **Navigation**: Simple state-based navigation (no external libraries)
- **Resources**: Shaders stored in `resources/shaders/`

## Source Structure

```
shared/
├── src/commonMain/
│   ├── kotlin/eu/gsegado/fragkit/
│   │   ├── ui/
│   │   │   ├── HomeScreen.kt
│   │   │   └── ShaderScreen.kt
│   │   ├── shader/
│   │   │   ├── ShaderLoader.kt     # expect fun loadShader()
│   │   │   └── ShaderRenderer.kt   # expect class with lifecycle methods
│   │   └── theme/
│   │       └── Theme.kt
│   └── resources/shaders/          # 📦 SHADERS DISTRIBUTION
│       ├── plasma.frag            # Sample plasma shader
│       └── vertex.glsl            # Vertex shader for quad
├── src/androidMain/
│   └── kotlin/eu/gsegado/fragkit/
│       ├── ui/ShaderScreen.android.kt
│       └── shader/
│           ├── ShaderLoader.android.kt    # Android asset loading
│           └── ShaderRenderer.android.kt  # GLES30 implementation
├── src/iosMain/
│   └── kotlin/eu/gsegado/fragkit/
│       └── shader/
│           └── ShaderLoader.ios.kt       # TODO: Bundle loading
└── src/desktopMain/
    └── kotlin/eu/gsegado/fragkit/
        └── shader/
            └── ShaderLoader.desktop.kt   # TODO: File loading
```

## Cross-Platform Architecture

### Shader Loader (Expect/Actual)

```kotlin
// commonMain
expect fun loadShader(assetPath: String): String

// androidMain
actual fun loadShader(assetPath: String): String {
    // Loads from assets/shaders/
    val context = appContext ?: throw IllegalStateException(...)
    val normalizedPath = if (assetPath.startsWith("shaders/")) {
        assetPath
    } else {
        "shaders/$assetPath"
    }
    val stream = context.assets.open(normalizedPath)
    return stream.bufferedReader().use { it.readText() }
}

// iosMain
actual fun loadShader(assetPath: String): String {
    // Loads from bundle
}

// desktopMain
actual fun loadShader(assetPath: String): String {
    // Loads from files
}
```

### Renderer Architecture (Expect/Actual)

```kotlin
// commonMain
expect class ShaderRenderer(shaderSource: String) {
    fun onSurfaceCreated(width: Int, height: Int)
    fun onDrawFrame(time: Float, width: Int, height: Int)
    fun onSurfaceDestroyed()
}

// Implementations:
// - androidMain: GLES30 (OpenGL ES 3.0)
// - iosMain: MTKView / Metal
// - desktopMain: GLFW + OpenGL 3.3
```

## Shaders

### Shader Structure

Shaders are stored in `resources/shaders/` and follow the GLSL ES 3.0 specification:

```glsl
#version 300 es
precision highp float;
uniform float u_time;
uniform vec2 u_resolution;
out vec4 FragColor;

void main() {
    // Your shader code here
}
```

### Uniforms

- `u_time`: Time in seconds since shader start (float)
- `u_resolution`: Viewport size in pixels (vec2)

### Output

- `FragColor`: Output color (vec4)

## Dependencies

The module depends on:
- Compose Multiplatform (runtime, foundation, material3, ui, components.resources)
- AndroidX Lifecycle (viewmodelCompose, runtimeCompose)

## Build Configuration

The module is configured with:
- `androidResources` enabled for resource support
- `isIncludeAndroidResources` enabled for tests
- Kotlin JVM Target 11
- Namespace: `eu.gsegado.fragkit.shared`

## Build Directives

### Resources Directory

Shaders are placed in `shared/src/commonMain/resources/shaders/` for cross-platform distribution.

### Build Architecture Directives

In `androidApp/build.gradle.kts`, resources from `shared/src/commonMain/resources` are automatically copied to Android assets:

```kotlin
android {
    sourceSets {
        named("main") {
            assets.srcDir("../shared/src/commonMain/resources")
        }
    }
}
```

## Testing

Module tests include:
- Common tests in `commonTest`
- Android resources included in tests (`isIncludeAndroidResources = true`)

## Contributing

When adding new shaders:
1. Place `.frag` files in `resources/shaders/`
2. Ensure they follow the GLSL ES 3.0 specification
3. Automatic loading support for Android via path normalization

Code conventions:
- Follow Kotlin naming conventions (`PascalCase` for classes, `camelCase` for functions)
- One file per main class/composable
- Compose Multiplatform for all UI
- Simple state-based navigation
- Platform-specific implementation via `expect`/`actual`

## License

This module is part of FragKit - a Kotlin Multiplatform shader rendering application.