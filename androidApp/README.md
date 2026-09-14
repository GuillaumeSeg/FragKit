# Android App Module

This module contains the Android-specific implementation of the FragKit application. It provides the entry point and main UI for shader rendering.

## Overview

The Android app serves as the main interface for users to load and view GLSL ES 3.0 fragment shaders. It features:
- **Home Screen**: A simple interface with a "Load Shader" button
- **Shader Screen**: Fullscreen rendering of GLSL ES 3.0 shaders
- **Shader Loading**: Loads `.frag` files from the assets directory (from the shared module)
- **Cross-platform Core**: Uses the shared module for all shader logic

## Architecture

```
androidApp/
├── src/main/
│   ├── java/eu/gsegado/fragkit/
│   │   ├── MainActivity.kt        # Entry point
│   │   └── FragKitApp.kt          # Main Compose application
│   ├── assets/                     # Copy of shared resources
│   └── res/                        # Android resources
├── build.gradle.kts                # Build configuration
└── shaders/                        # (Historical) Previous shaders
```

### Shader Path

Since v2.0, shaders are stored in the **shared module** (`shared/src/commonMain/resources/shaders/`).
The Gradle build automatically copies these resources to `androidApp/src/main/assets/shaders/`.

## Key Components

### MainActivity.kt
- Android application entry point
- Initializes the application context for shader loading
- Sets up Compose content

### FragKitApp.kt
- Main composable function that manages app state
- Handles navigation between HomeScreen and ShaderScreen
- Manages the ShaderRenderer lifecycle

### Shader Loading
- Shaders are loaded from `assets/shaders/` (copied from shared)
- Uses the `loadShader()` function from the shared module
- Access path is normalized (adds `shaders/` if necessary)
- Application context is initialized in MainActivity

## Dependencies

The app depends on:
- `:shared` - Cross-platform shader rendering logic
- Compose Multiplatform - UI framework
- AndroidX Activity Compose - Activity integration
- Compose UI Tooling (debug) - UI debugging

## Build

To build the Android app:

```bash
# Build debug APK
./gradlew :androidApp:assembleDebug

# Install and run on device/emulator
./gradlew :androidApp:installDebug
```

### Build Configuration

Shared module resources are automatically included:

```kotlin
android {
    sourceSets {
        named("main") {
            assets.srcDir("../shared/src/commonMain/resources")
        }
    }
}
```

## Adding New Shaders

**IMPORTANT**: Since v2.0, shaders must be placed in the shared module!

1. Create a new `.frag` file in `shared/src/commonMain/resources/shaders/`
2. Ensure it follows the GLSL ES 3.0 specification:
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
3. Rebuild the Gradle build to copy resources
4. Update the HomeScreen to load the new shader

### Access Path

Shaders are accessed via:
- Common path: `"shaders/your-shader.frag"`
- Android implementation automatically adds `"shaders/"` if necessary

## File Structure

- `MainActivity.kt` - Application entry point
- `FragKitApp.kt` - Composable application and state management
- `build.gradle.kts` - Android module configuration
- `assets/shaders/` - GLSL shaders (copied from shared)
  - `plasma.frag` - Example plasma shader
  - `vertex.glsl` - Vertex shader for quad fullscreen rendering

## Notes

- The app uses simple state-based navigation (no navigation library)
- All shader logic is in the shared module for cross-platform compatibility
- The app requires API level 26 or higher (Android 8.0)
- Shaders must be GLSL ES 3.0 compliant with `u_time` and `u_resolution` uniforms
- Shader loading uses path normalization to support the shared structure

## Development

### Running on Device
1. Connect an Android device via USB
2. Enable USB debugging
3. Run: `./gradlew :androidApp:installDebug`

### Running on Emulator
1. Open Android Studio
2. Create or select an emulator
3. Run the app from Android Studio or command line

### Debugging
- Check Logcat for shader compilation logs
- Look for tags: `ShaderRenderer`, `ShaderLoader`
- Shader compilation errors are detailed in the logs

### Adding New Features
1. Add UI in `shared/src/commonMain/`
2. Add platform-specific logic in `androidApp/`
3. Use `expect`/`actual` for platform-dependent code
4. Test on Android before porting to other platforms

## Contributing

When adding new features or modifying the app:

1. Follow the existing code conventions from `AGENTS.md`
2. Keep UI in Compose Multiplatform
3. Place shader logic in the shared module
4. Use `expect`/`actual` for platform-specific code
5. Test on Android before porting to other platforms
6. Don't forget to update shaders in `shared/src/commonMain/resources/shaders/`