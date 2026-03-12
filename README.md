# Pokedex App

A modern, secure, and robust Pokedex application built with Kotlin and Jetpack Compose. This app allows users to explore the world of Pokemon, search for their favorites, and manage a personalized list of favorite Pokemon with local data persistence and account-based isolation.

## 🎯 Project Goals
The main goal of this project is to demonstrate modern Android development practices, including:
- Implementing a scalable and maintainable architecture using MVVM and Repository patterns.
- Ensuring data security through database encryption and biometric authentication.
- Providing a smooth and reactive user experience using declarative UI.
- Maintaining high code reliability through a comprehensive unit testing suite.

---

## 🏗 Architecture
The project follows the **MVVM (Model-View-ViewModel)** architectural pattern combined with a **Repository Pattern** to ensure a clean separation of concerns and testability:

- **UI Layer**: Built entirely with **Jetpack Compose**, utilizing state-driven UI components that react to ViewModel state changes.
- **ViewModel Layer**: Manages UI state using `StateFlow` and handles business logic by interacting with repositories. It uses `viewModelScope` for coroutine management.
- **Repository Layer**: Acts as a single source of truth, coordinating data between the remote PokeAPI and the local encrypted database.
- **Data Layer**:
  - **Remote**: Uses **Retrofit** and **OkHttp** for efficient network communication with PokeAPI.
  - **Local**: Uses **Room Database** for persistence, secured with **SQLCipher**.
  - **Session**: Uses **EncryptedSharedPreferences** and **DataStore** for secure user session management.

---

## 📚 Tech Stack & Libraries
The application leverages several industry-standard libraries:

| Category | Library | Purpose |
| --- | --- | --- |
| **UI** | Jetpack Compose | Modern toolkit for building native UI. |
| **Navigation** | Navigation Compose | Handling type-safe navigation between screens. |
| **DI** | Koin | Lightweight dependency injection framework for managing object lifecycles. |
| **Networking** | Retrofit & OkHttp | Consuming the PokeAPI with efficient request handling and logging. |
| **Database** | Room | SQLite abstraction for local data persistence. |
| **Security** | SQLCipher | Full database encryption to protect data at rest. |
| **Security** | Security-Crypto | Encrypted storage for sensitive session tokens and user data. |
| **Auth** | Biometric | Fingerprint and face unlock integration for secure user access. |
| **Images** | Coil | Modern image loading library for high-performance Pokemon sprite rendering. |
| **Pagination** | Paging 3 | Efficiently loading large lists of Pokemon with automated caching. |
| **Testing** | MockK & JUnit 4 | Frameworks used for mocking dependencies and structuring unit tests. |

---

## ✨ Key Features
- **Secure Authentication**: Register and Login system with **SHA-256 password hashing**.
- **Biometric Login**: Integration with system biometric prompts for quick, secure access.
- **Pokemon Explorer**: Browse a paginated list of over 1000+ Pokemon with automated "end-of-list" loading.
- **Optimized Search**: Real-time search by name featuring a **500ms debounce** to minimize unnecessary API/DB calls.
- **Account-Based Favorites**: Bookmark Pokemon to your personal list. Favorites are **isolated per user**, ensuring your data is private and unique to your account.
- **Search History**: Local tracking of recent search queries for a better user experience.
- **Detailed Statistics**: Comprehensive view of Pokemon types, base stats, and high-quality artwork.
- **Data Security**: Every piece of local data is encrypted at rest using **SQLCipher** and a unique secret key.

---

## 🧪 Testing
The project includes a robust suite of **Unit Tests** to ensure business logic remains correct as the app evolves.

### **Unit Testing Strategy:**
- **ViewModel Tests**: Validating UI state transitions (Loading, Success, Error) and business logic (Search debounce, Auth flow). We use `runTest` and `MainDispatcherRule` to test `StateFlow` emissions.
- **Repository Tests**: Testing data coordination and mapping from API/DB models, including verifying user-specific data isolation in the Favorites module.
- **Utility Tests**: Logic-heavy helpers, such as the `PasswordUtil` (SHA-256) and string normalizers, are tested for 100% accuracy.
- **Session Management**: Tests verify that user sessions are correctly saved, cleared, and retrieved from encrypted storage.

### **How to Run Unit Tests:**
To run the full unit test suite for the development build, use the following command in your terminal:

```bash
./gradlew testDevelopmentDebugUnitTest
```

---

## 🚀 Getting Started
1. Clone the repository.
2. Ensure you have the `BASE_URL`, `IMAGE_URL`, and `DB_SECRET` defined in your `local.properties` file.
3. Sync the project with Gradle.
4. Build and run the app on an Android device or emulator (API 24 or higher required).

---
