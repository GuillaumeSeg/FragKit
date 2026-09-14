# AGENTS.md

## Project
**FragKit** — Kotlin Multiplatform app (Android / iOS / Desktop) that loads
and renders fragment shaders (GLSL) fullscreen.
Focus: KMP architecture, expect/actual, Compose Multiplatform.
Content: procedural shaders (generative art, GPU experiments).

## Package
`eu.gsegado.fragkit`

## Structure

FragKit/
├── shared/
│ ├── src/commonMain/kotlin/eu/gsegado/fragkit/
│ │ ├── ui/
│ │ │ ├── HomeScreen.kt ← Big "Load Shader" button on dark background
│ │ │ └── ShaderScreen.kt ← Fullscreen preview (landscape/portrait)
│ │ ├── shader/
│ │ │ ├── ShaderRenderer.kt ← expect class for cross-platform shader rendering
│ │ │ └── ShaderLoader.kt ← expect/actual : loads .frag from assets
│ │ └── theme/
│ │ └── Theme.kt ← Dark theme
│ ├── src/androidMain/kotlin/eu/gsegado/fragkit/
│ │ ├── ui/ShaderScreen.android.kt ← Android implementation using GLSurfaceView
│ │ ├── shader/ShaderRenderer.android.kt ← Android GLES30 implementation
│ │ └── shader/ShaderLoader.android.kt ← Android asset loading
│ ├── src/iosMain/kotlin/eu/gsegado/fragkit/
│ │ └── shader/ShaderLoader.ios.kt
│ └── src/desktopMain/kotlin/eu/gsegado/fragkit/
│ └── shader/ShaderLoader.desktop.kt
├── androidApp/
│ ├── src/main/kotlin/eu/gsegado/fragkit/
│ │ ├── MainActivity.kt ← Entry point
│ │ └── FragKitApp.kt ← Main Compose app with navigation
│ └── src/main/assets/shaders/
│ └── plasma.frag ← Sample shader
├── iosApp/ ← Xcode project, ComposeViewController
├── desktopApp/ ← Later
└── shaders/ ← .frag files (assets)

## Conventions

- **UI** : Compose Multiplatform everywhere (including iOS via `ComposeViewController`)
- **Navigation** : simple, 2 screens, no nav library (Navigation Compose or simple state)
- **State** : `StateFlow` / `mutableStateOf`, no ViewModel framework (Koin, etc.)
- **Shader** : GLSL ES 3.0 (fragment), uniforms : `u_time`, `u_resolution`
- **Naming** : `PascalCase` for composables and classes, `camelCase` for functions/props
- **Files** : one file = one main class/composable

## Shader Loading

```kotlin
// commonMain
expect fun loadShader(assetPath: String): String 

// androidMain
actual fun loadShader(assetPath: String): String =
    context.assets.open(assetPath).bufferedReader().use { it.readText() }

// iosMain
actual fun loadShader(assetPath: String): String =
    String(NSBundle.mainBundle.contentsOfFile(
        NSBundle.mainBundle.pathForResource(assetPath, null)!!,
        NSDataEncoding.UTF8))

// desktopMain
actual fun loadShader(assetPath: String): String =
    File("shaders/$assetPath").readText()
```

## Build

### Android
./gradlew :androidApp:assembleDebug

### iOS (via Xcode)
./gradlew :shared:embedAndSignAppleFrameworkForXcode

### Desktop
./gradlew :desktopApp:run

## Boundaries

### NEVER
- Add external dependencies without discussion
- Use OpenGL/Vulkan directly in commonMain (use expect/actual if needed)
- Modify iosApp/iosApp.xcodeproj manually

### ALWAYS
- Place shader logic in shared/src/commonMain
- Put .frag files in shaders/ (project root)
- Test shader on Android before porting to iOS/Desktop
- **Place shader files in shared/src/commonMain/resources/shaders/** for cross-platform distribution

## GLSL Context

- Fragment shaders only (no custom vertex)
- Standard uniforms: u_time (float), u_resolution (vec2)
- No textures in v1 (100% procedural)
- .frag files are raw text, no preprocessing

## Rendering

### Shader Contract (GLSL ES 3.0)

Each `.frag` must respect:
```glsl
precision highp float;
uniform float u_time;
uniform vec2 u_resolution;
void main() {
    // ...
}
```

### Backend by Platform

| Platform | View | API |
|----------|------|-----|
| Android | GLSurfaceView (ES 3.0) via AndroidView | min API 26 |
| iOS | MTKView (Metal) | min iOS 14 |
| Desktop | GLFW + OpenGL 3.3 | - |

### Renderer Architecture (expect/actual)

```kotlin
// commonMain
expect class ShaderRenderer(shaderSource: String) {
    fun onSurfaceCreated(width: Int, height: Int)
    fun onDrawFrame(time: Float, width: Int, height: Int)
    fun onSurfaceDestroyed()
}

// androidMain → implemented with GLES30
// iosMain     → implemented with MTKView / MTLDevice
// desktopMain → implemented with GLFW + GL 3.3
```

### Uniforms

- u_time : seconds since shader start (float)
- u_resolution : viewport size in pixels (vec2)
- No textures in v1 (100% procedural)

## Documentation and Comments

### Language Requirement
- **Documentation and code comments MUST be written in English only**
- This applies to:
  - README files (root, shared/, androidApp/)
  - Code comments (KDoc, inline comments)
  - Commit messages
  - Documentation strings
  - Any project documentation

### Documentation Guidelines
- Project documentation: Root README.md, module READMEs
- Code documentation: KDoc comments for public APIs
- Inline comments: Explain non-obvious code logic
- Commit messages: Use clear English descriptions

## Development Workflow

### Adding New Shaders (v2.0)
**IMPORTANT**: Shaders must be placed in `shared/src/commonMain/resources/shaders/`!

1. Create a new `.frag` file in `shared/src/commonMain/resources/shaders/`
2. Ensure it follows the GLSL ES 3.0 specification
3. Run the Gradle build to copy resources to Android
4. Shaders are automatically available in `androidApp/src/main/assets/shaders/`
5. Access via: `"shaders/your-shader.frag"`

### Platform-Specific Code Locations
- **Shared UI**: `shared/src/commonMain/kotlin/eu/gsegado/fragkit/ui/`
- **Shared Shader Logic**: `shared/src/commonMain/kotlin/eu/gsegado/fragkit/shader/`
- **Shared Resources**: `shared/src/commonMain/resources/`
- **Android Implementation**: `shared/src/androidMain/` + `androidApp/`
- **iOS Implementation**: `shared/src/iosMain/` + `iosApp/`
- **Desktop Implementation**: `shared/src/desktopMain/` + `desktopApp/`
