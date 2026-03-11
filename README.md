# Pokedex App

A modern, secure, and robust Pokedex application built with Kotlin and Jetpack Compose. This app allows users to explore the world of Pokemon, search for their favorites, and manage a personalized list of favorite Pokemon with local data persistence.

## 🎯 Project Goals
The main goal of this project is to demonstrate modern Android development practices, including:
- Implementing a scalable and maintainable architecture.
- Ensuring data security through database encryption and biometric authentication.
- Providing a smooth and reactive user experience using declarative UI.
- Maintaining high code quality through comprehensive unit testing.

---

## 🏗 Architecture
The project follows the **MVVM (Model-View-ViewModel)** architectural pattern combined with a **Repository Pattern** to ensure a clean separation of concerns:

- **UI Layer**: Built entirely with **Jetpack Compose**, using state-driven UI components.
- **ViewModel Layer**: Manages UI state and business logic, leveraging `StateFlow` and `Coroutines` for reactive data handling.
- **Repository Layer**: Acts as a single source of truth, coordinating data between the remote API and the local database.
- **Data Layer**: 
    - **Remote**: Handles network requests to the PokeAPI using Retrofit.
    - **Local**: Manages persistence using Room Database and DataStore for session management.

---

## 📚 Tech Stack & Libraries
The application leverages several industry-standard libraries:

| Category | Library | Purpose |
| --- | --- | --- |
| **UI** | Jetpack Compose | Modern toolkit for building native UI. |
| **Navigation** | Navigation Compose | Handling navigation between screens. |
| **DI** | Koin | Lightweight dependency injection framework. |
| **Networking** | Retrofit & OkHttp | Consuming the PokeAPI and logging network traffic. |
| **Database** | Room | SQLite abstraction for local data persistence. |
| **Security** | SQLCipher | Encrypting the Room database using `SupportOpenHelperFactory`. |
| **Security** | Security-Crypto | Encrypted SharedPreferences for session management. |
| **Auth** | Biometric | Providing fingerprint/face unlock for secure login. |
| **Images** | Coil | Fast and asynchronous image loading. |
| **Pagination** | Paging 3 | Loading large lists of Pokemon efficiently. |
| **Async** | Coroutines & Flow | Managing background tasks and reactive data streams. |

---

## ✨ Key Features
- **User Authentication**: Secure Register and Login flow with password hashing (SHA-256).
- **Biometric Login**: Quick and secure access using Android Biometric prompt.
- **Pokemon Explorer**: Browse a paginated list of Pokemon with high-quality images.
- **Deep Search**: Search Pokemon by name with a **500ms debounce** to optimize performance.
- **Search History**: Tracks recent searches locally for quick access.
- **Favorites System**: Bookmark Pokemon to access them offline.
- **Detailed Stats**: View specific details, types, and stats for every Pokemon.
- **Secure Database**: All local data is encrypted at rest using a dedicated secret key.

---

## 🧪 Unit Testing
Testing is a core part of this project. We use **MockK** for mocking dependencies and **Kotlinx-Coroutines-Test** for handling asynchronous logic in tests.

### **Testing Coverage:**
- **ViewModels**: Validating UI state transitions (Loading, Success, Error) and business logic (Search debounce, Auth flow).
- **Repositories**: Testing data coordination and mapping from API/DB models.
- **Utilities**: Verifying helper functions like password hashing and string normalization.

### **How to Run Tests:**
To execute all unit tests, run the following command in your terminal:
```bash
./gradlew test
```

---

## 🚀 Getting Started
1. Clone the repository.
2. Ensure you have the `BASE_URL`, `IMAGE_URL`, `DB_SECRET` defined in your `local.properties` or environment variables for database encryption.
3. Build and run the app on an Android device or emulator (API 24+ recommended).

---

Developed with ❤️ by PokedexApp.
