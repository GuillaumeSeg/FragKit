# FragKit

**FragKit** — Kotlin Multiplatform app (Android / iOS / Desktop) that loads and renders fragment shaders (GLSL) fullscreen.

## Current Implementation Status

- ✅ **Android App**: Complete and functional
- 🚧 **iOS App**: Not yet implemented
- 🚧 **Desktop App**: Not yet implemented
- ✅ **Shared Module**: Core shader loading and rendering logic

## Focus Areas

- **KMP Architecture**: Proper use of `expect`/`actual` for cross-platform compatibility
- **Compose Multiplatform**: Unified UI across all platforms
- **Shader Rendering**: GLSL ES 3.0 fragment shaders with `u_time` and `u_resolution` uniforms

## Project Structure

```
FragKit/
├── shared/
│   ├── src/commonMain/     # Shared code (UI, shader logic)
│   ├── src/androidMain/    # Android-specific implementations
│   ├── src/iosMain/        # iOS-specific implementations (TODO)
│   └── src/desktopMain/    # Desktop-specific implementations (TODO)
├── androidApp/             # Android application (complete)
├── iosApp/                 # iOS application (Xcode project)
├── desktopApp/             # Desktop application (TODO)
└── shaders/                # Sample GLSL fragment shaders
```

## Key Features

### Shared Module (`shared/`)
- **UI Components**: HomeScreen, ShaderScreen (Compose Multiplatform)
- **Shader Handling**: ShaderRenderer (expect/actual), ShaderLoader (expect/actual)
- **Theme**: Dark, minimal theme implementation
- **Navigation**: Simple state-based navigation (no external libraries)

### Android Implementation (`androidApp/`)
- **Entry Point**: MainActivity.kt
- **App Logic**: FragKitApp.kt (state management, navigation)
- **Assets**: Shaders stored in `src/main/assets/shaders/`
- **Shader Rendering**: 
  - Vertex shader: `shaders/vertex.glsl` (fullscreen quad)
  - Fragment shader: `shaders/plasma.frag` (example plasma effect)
- **Dependencies**: Compose Multiplatform, AndroidX

## Shader Requirements

All fragment shaders must follow the GLSL ES 3.0 specification:

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
- `u_time`: Seconds since shader start (float)
- `u_resolution`: Viewport size in pixels (vec2)

### Output
- `FragColor`: Output color (vec4)

## Building and Running

### Android App
```bash
# Build debug APK
./gradlew :androidApp:assembleDebug

# Install and run on connected device/emulator
./gradlew :androidApp:installDebug
```

### Requirements
- Android API level 26+ (Android 8.0)
- OpenGL ES 3.0 support

## Development Guidelines

### Adding New Shaders
1. Place `.frag` files in `androidApp/src/main/assets/shaders/`
2. Ensure they follow the GLSL ES 3.0 specification above
3. Update the HomeScreen to load the new shader (currently loads `plasma.frag`)

### Cross-Platform Development
- Keep UI code in `shared/src/commonMain/`
- Place platform-specific code in respective `*Main/` source sets
- Use `expect`/`actual` for platform-dependent functionality
- Test on Android first before implementing iOS/Desktop

### Code Conventions
- Follow Kotlin naming conventions (`PascalCase` for classes, `camelCase` for functions)
- One file per main class/composable
- Compose Multiplatform for all UI
- Simple state-based navigation

## Sample Shader

The project includes a sample plasma shader that demonstrates:
- Time-based animation (`u_time`)
- Resolution-aware rendering (`u_resolution`)
- Procedural color generation
- Sine wave patterns

## Next Steps

1. **Complete iOS Implementation**:
   - Implement `ShaderRenderer.ios.kt` using MTKView/Metal
   - Implement `ShaderLoader.ios.kt` for bundle loading
   - Create iOS app with ComposeViewController

2. **Complete Desktop Implementation**:
   - Implement `ShaderRenderer.desktop.kt` using GLFW + OpenGL 3.3
   - Implement `ShaderLoader.desktop.kt` for file loading
   - Create desktop application

3. **Enhance Features**:
   - Add shader selection UI
   - Implement uniform controls
   - Add shader compilation error display
   - Support for multiple shader passes

## License

This project is part of FragKit - a Kotlin Multiplatform shader rendering application.

## Acknowledgments

- Inspired by shadertoy.com and similar platforms
- Built with Kotlin Multiplatform and Compose Multiplatform