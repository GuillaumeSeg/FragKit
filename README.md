# FragKit

**FragKit** — Kotlin Multiplatform app (Android / iOS / Desktop) that loads and renders fragment shaders (GLSL) fullscreen.

## 📋 Overview

FragKit is a Kotlin Multiplatform (KMP) application that loads and displays GLSL ES 3.0 fragment shaders fullscreen. The architecture uses `expect`/`actual` for complete cross-platform compatibility between Android, iOS, and Desktop.

### ✅ Current Implementation Status

- ✅ **Android App**: Complete and functional
- ✅ **Shared Module**: Core shader logic with shaders in commonMain resources
- 🚧 **iOS App**: Not yet implemented (project structure ready)
- 🚧 **Desktop App**: Not yet implemented (project structure ready)

### 🎯 Focus Areas

- **KMP Architecture**: Proper use of `expect`/`actual` for cross-platform compatibility
- **Compose Multiplatform**: Unified UI across all platforms
- **Shader Distribution**: Shaders in `shared/src/commonMain/resources/` for distribution
- **Android Integration**: Automatic copy of shared resources to `androidApp/src/main/assets/`

## 📁 Project Structure

```
FragKit/
├── shared/                            # KMP Core Module (NEW)
│   ├── src/commonMain/
│   │   ├── kotlin/eu/gsegado/fragkit/
│   │   │   ├── ui/
│   │   │   │   ├── HomeScreen.kt
│   │   │   │   └── ShaderScreen.kt
│   │   │   ├── shader/
│   │   │   │   ├── ShaderLoader.kt     # expect fun loadShader()
│   │   │   │   └── ShaderRenderer.kt   # expect class with lifecycle methods
│   │   │   └── theme/
│   │   │       └── Theme.kt
│   │   └── resources/shaders/          # 📦 SHADERS DISTRIBUTION (NEW)
│   │       ├── plasma.frag            # Sample plasma shader
│   │       └── vertex.glsl            # Vertex shader for quad
│   ├── src/androidMain/
│   │   └── kotlin/eu/gsegado/fragkit/
│   │       ├── ui/ShaderScreen.android.kt
│   │       └── shader/
│   │           ├── ShaderLoader.android.kt    # Android asset loading
│   │           └── ShaderRenderer.android.kt  # GLES30 implementation
│   ├── src/iosMain/
│   │   └── kotlin/eu/gsegado/fragkit/
│   │       └── shader/
│   │           └── ShaderLoader.ios.kt       # TODO: Bundle loading
│   └── src/desktopMain/
│       └── kotlin/eu/gsegado/fragkit/
│           └── shader/
│               └── ShaderLoader.desktop.kt   # TODO: File loading
│
├── androidApp/                         # Android Application (Complete)
│   ├── src/main/
│   │   ├── java/eu/gsegado/fragkit/
│   │   │   ├── MainActivity.kt
│   │   │   └── FragKitApp.kt
│   │   ├── assets/shaders/             # Copied from shared (automatic)
│   │   │   ├── plasma.frag
│   │   │   └── vertex.glsl
│   │   └── res/
│   └── build.gradle.kts                # Config: copies shared resources
│
├── iosApp/                             # iOS Application (Xcode)
├── desktopApp/                         # Desktop Application (TODO)
└── shaders/                            # (Historical - obsolete)
```

## 🔑 Key Features

### Shared Module (`shared/`)

The heart of the application with all shared shaders:

- **UI Components**: HomeScreen, ShaderScreen (Compose Multiplatform)
- **Shader Handling**: 
  - `ShaderLoader` (expect/actual) - Handles cross-platform loading
  - `ShaderRenderer` (expect/actual) - Handles graphical rendering
- **Theme**: Dark, minimal theme implementation
- **Navigation**: Simple state-based navigation (no external libraries)
- **Resources**: Shaders stored in `resources/shaders/` 📦

### Shader Loading Architecture

```kotlin
// Common path from shared code
loadShader("shaders/plasma.frag")

// Android implementation:
// 1. Shader found in shared/src/commonMain/resources/shaders/
// 2. Automatically copied to androidApp/src/main/assets/shaders/
// 3. Loaded with context.assets.open("shaders/plasma.frag")
```

### Android Integration

The build automatically copies shared module resources:

```kotlin
// In androidApp/build.gradle.kts
android {
    sourceSets {
        named("main") {
            assets.srcDir("../shared/src/commonMain/resources")
        }
    }
}
```

### Android Implementation (`androidApp/`)

- **Entry Point**: MainActivity.kt
- **App Logic**: FragKitApp.kt (state management, navigation)
- **Assets**: Shaders copied from shared → `assets/shaders/`
- **Dependencies**: Compose Multiplatform, AndroidX

## 🎨 Shader Requirements

All GLSL ES 3.0 shaders follow the specification:

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

- `u_time`: Time in seconds since start (float)
- `u_resolution`: Viewport size in pixels (vec2)

### Current Shaders

- `plasma.frag` - Example plasma with time-based animation
- `vertex.glsl` - Vertex shader for quad fullscreen rendering

## 🚀 Building and Running

### Android App

```bash
# Build debug APK
./gradlew :androidApp:assembleDebug

# Install and run
./gradlew :androidApp:installDebug
```

### Requirements

- Android API level 26+ (Android 8.0)
- OpenGL ES 3.0 support
- Kotlin 1.9+
- JDK 11+

## 📝 Adding New Shaders (v2.0)

**IMPORTANT**: Shaders MUST be placed in `shared/src/commonMain/resources/shaders/`!

1. Create a new `.frag` file in `shared/src/commonMain/resources/shaders/`
2. Ensure it follows the GLSL ES 3.0 specification
3. Run the Gradle build to copy resources to Android
4. Shaders are automatically available in `androidApp/src/main/assets/shaders/`
5. Access via: `"shaders/your-shader.frag"`

### Access Path

- Since shared code: `"shaders/your-shader.frag"`
- Android implementation automatically normalizes the path

## 🌐 Development Guidelines

### Cross-Platform Development

- ✅ **Keep UI code** in `shared/src/commonMain/`
- ✅ **Place shader logic** in the shared module
- ✅ **Use expect/actual** for platform-dependent code
- ✅ **Test on Android** first before implementing iOS/Desktop
- ✅ **Shaders in shared** resources for distribution

### Code Conventions

- Follow Kotlin naming conventions (`PascalCase` for classes, `camelCase` for functions)
- One file per main class/composable
- Compose Multiplatform for all UI
- Simple state-based navigation
- Function name: `loadShader(assetPath: String): String`

### Architecture Directives

| Component | Location | Notes |
|-----------|----------|-------|
| UI | `shared/src/commonMain/` | Compose Multiplatform |
| Shader Logic | `shared/` | Expect/actual pattern |
| Android Implementation | `shared/src/androidMain/`, `androidApp/` | Platform-specific |
| Shaders | `shared/src/commonMain/resources/shaders/` | Distribution source 📦 |
| Android Assets | `androidApp/src/main/assets/shaders/` | Auto-copied from shared |

## 📚 Documentation

- 📖 [Shared Module README](shared/README.md) - Architecture and API of the shared module
- 📖 [Android App README](androidApp/README.md) - Configuration and Android development

## 🛣️ Next Steps

1. **Complete iOS Implementation**:
   - Implement `ShaderRenderer.ios.kt` using MTKView/Metal
   - Implement `ShaderLoader.ios.kt` for bundle loading
   - Create iOS app with ComposeViewController

2. **Complete Desktop Implementation**:
   - Implement `ShaderRenderer.desktop.kt` using GLFW + OpenGL 3.3
   - Implement `ShaderLoader.desktop.kt` for file loading
   - Create desktop application

3. **Enhance Features**:
   - Add shader selection UI with options
   - Implement uniform controls (time scale, resolution, etc.)
   - Add shader compilation error display with line numbers
   - Support for multiple passes (Multi-pass shaders)
   - Add shader preview/comparison

4. **Improve Developer Experience**:
   - Add KDoc documentation to all public APIs
   - Create sample shaders library
   - Add unit tests for shader loading
   - Performance profiling and optimization

## 🤝 Contributing

When adding new features:

1. **UI Components**: Place in `shared/src/commonMain/`
2. **Shader Logic**: Place in the shared module
3. **Platform-Specific Code**: Place in `*Main/` source sets
4. **Expect/Actual**: Use this pattern for cross-platform
5. **Shaders**: Place in `shared/src/commonMain/resources/shaders/` 📦
6. **Test on Android** before porting to other platforms
7. **Documentation**: Update corresponding READMEs

## 📄 License

This project is part of FragKit - a Kotlin Multiplatform shader rendering application.

## 🙏 Acknowledgments

- Inspired by shadertoy.com and similar platforms
- Built with Kotlin Multiplatform and Compose Multiplatform
- Shader distribution via shared KMP module for true cross-platform support 📦