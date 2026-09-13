# Android App Module

This module contains the Android-specific implementation of the FragKit application. It provides the entry point and main UI for the shader rendering app.

## Overview

The Android app serves as the main interface for users to load and view fragment shaders. It features:

- **Home Screen**: A simple interface with a "Load Shader" button
- **Shader Screen**: Fullscreen rendering of GLSL ES 3.0 fragment shaders
- **Shader Loading**: Loads `.frag` files from the assets directory
- **Cross-platform Core**: Uses the shared module for all shader logic

## Architecture

```
androidApp/
├── src/main/
│   ├── java/eu/gsegado/fragkit/
│   │   ├── MainActivity.kt        # Entry point
│   │   └── FragKitApp.kt          # Main Compose app
│   └── assets/shaders/
│       ├── plasma.frag            # Example shader
│       └── vertex.glsl            # Vertex shader for fullscreen quad
```

## Key Components

### MainActivity.kt
- Entry point of the Android application
- Initializes the application context for shader loading
- Sets up the Compose content

### FragKitApp.kt
- Main composable function that manages app state
- Handles navigation between HomeScreen and ShaderScreen
- Manages the ShaderRenderer lifecycle

### Shader Loading
- Shaders are loaded from `assets/shaders/` directory
- Uses `loadShader()` function from the shared module
- Application context is initialized in MainActivity

## Dependencies

The app depends on:
- `:shared` module - Core shader rendering logic
- Compose Multiplatform - UI framework
- AndroidX Activity Compose - Activity integration

## Building

To build the Android app:

```bash
./gradlew :androidApp:assembleDebug
```

To run the app on an emulator or device:

```bash
./gradlew :androidApp:installDebug
```

## Adding New Shaders

1. Create a new `.frag` file in `androidApp/src/main/assets/shaders/`
2. Follow the GLSL ES 3.0 specification:
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
3. Update the `HomeScreen` to load the new shader

## File Structure

- `MainActivity.kt` - Application entry point
- `FragKitApp.kt` - Main composable and state management
- `assets/shaders/` - GLSL fragment shader files
  - `plasma.frag` - Example plasma shader
  - `vertex.glsl` - Vertex shader for fullscreen quad rendering

## Notes

- The app uses a simple state-based navigation (no navigation library)
- All shader logic is in the shared module for cross-platform compatibility
- The app requires API level 26 or higher (Android 8.0)
- Shaders must be GLSL ES 3.0 compliant with `u_time` and `u_resolution` uniforms

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
- Shader compilation errors will be logged in detail

## Contributing

When adding new features or modifying the app:

1. Follow the existing code conventions from `AGENTS.md`
2. Keep UI in Compose Multiplatform
3. Place shader logic in the shared module
4. Use expect/actual for platform-specific code
5. Test on Android before porting to other platforms

## License

This project is part of FragKit - a Kotlin Multiplatform shader rendering application.