# Togeda Android App

A modern Android application built with Jetpack Compose, following MVVM architecture and using OpenAPI-generated API clients.

## Prerequisites

- Android Studio Hedgehog or later
- JDK 11 or later
- Android SDK 36
- Minimum Android API level 24

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd Togeda
```

### 2. Open in Android Studio

Open the project in Android Studio and let it sync.

### 3. Generate API Client

The project uses OpenAPI-generated API clients. These are automatically generated during the build process, but you can also generate them manually:

```bash
# Generate API client
./gradlew openApiGenerate

# Or clean and regenerate
./gradlew openApiRegenerate
```

### 4. Build the Project

```bash
./gradlew assembleDebug
```

## Project Structure

```
app/src/main/
├── assets/api/
│   └── api-docs.yaml          # OpenAPI specification
├── java/com/togeda/app/
│   ├── data/
│   │   ├── mapper/            # Data mapping
│   │   ├── remote/            # API interfaces
│   │   ├── repository/        # Repository implementations
│   │   └── security/          # Security utilities
│   ├── di/                    # Dependency injection
│   ├── domain/
│   │   ├── model/             # Domain models
│   │   ├── repository/        # Repository interfaces
│   │   └── usecase/           # Use cases
│   └── presentation/
│       ├── common/            # Shared UI components
│       ├── eventdetails/      # Event details screen
│       ├── feed/              # Feed screen
│       ├── login/             # Login screen
│       └── navigation/        # Navigation
```

## Generated Files

The following files are automatically generated from the OpenAPI specification:

- `com.togeda.app.data.remote.generated.*` - API client interfaces
- `com.togeda.app.data.model.generated.*` - Data models
- `org.openapitools.client.infrastructure.*` - HTTP client infrastructure

These files are generated in `app/build/generated/openapi/src/main/kotlin/` and are automatically included in the build.

## Troubleshooting

### Missing Generated Files

If you encounter compilation errors related to missing generated API classes:

1. Clean the project: `./gradlew clean`
2. Regenerate API client: `./gradlew openApiRegenerate`
3. Rebuild: `./gradlew assembleDebug`

### Build Issues

- Ensure you have JDK 11 or later installed
- Make sure Android SDK 36 is installed
- Check that all dependencies are properly synced

### Runtime Issues

#### TokenManager Keystore Error

If you encounter a crash related to `TokenManager` and `EncryptedSharedPreferences`, it's likely due to keystore corruption. The app now includes automatic fallback to regular `SharedPreferences` for this scenario.

**Symptoms:**
```
Caused by: javax.crypto.AEADBadTagException
Caused by: android.security.KeyStoreException: Signature/MAC verification failed
```

**Solutions:**
1. **Automatic**: The app will automatically fallback to unencrypted storage
2. **Manual Reset**: Clear app data in Android Settings
3. **Debug**: Use `TokenManager.resetAllData()` in development

**Note**: The fallback mechanism ensures the app continues to work even with keystore issues, though with reduced security for stored tokens.

## Architecture

- **MVVM**: Model-View-ViewModel architecture
- **Repository Pattern**: Data access abstraction
- **Dependency Injection**: Koin for DI
- **Jetpack Compose**: Modern UI toolkit
- **Coroutines**: Asynchronous programming
- **Retrofit**: HTTP client for API calls

## Dependencies

- **UI**: Jetpack Compose, Material 3
- **Networking**: Retrofit, OkHttp
- **DI**: Koin
- **Image Loading**: Coil
- **Serialization**: Jackson, Gson
- **Async**: Kotlin Coroutines 