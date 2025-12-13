# Copilot Instructions for Android Development

## 🌍 Language Guidelines

- **Source Code**: All source code (classes, methods, variables, comments) MUST be written in English.
- **String Resources**: Text strings can be in multiple languages and should be stored in appropriate `strings.xml` files for each locale.
- **Documentation**: Code documentation and comments should be in English.

## 🏗️ Architecture Overview

- Follow **Android Official Architecture** guidelines.
- The application uses a **three-layer architecture**:
    - **Presentation Layer**: UI components and ViewModels
    - **Domain Layer**: Business logic, use cases, and repository interfaces
    - **Data Layer**: Data sources and repository implementations

## 📱 Presentation Layer

### Activity & Navigation
- Use **Single Activity** pattern.
- Use **Fragments** to create screens/views.
- Navigation between screens should use the Navigation Component.

### MVVM Pattern
- Implement **MVVM (Model-View-ViewModel)** pattern.
- Each Fragment should have its corresponding ViewModel.
- ViewModels should extend `androidx.lifecycle.ViewModel`.

### Data Flow & Observation
- Use **LiveData**, **StateFlow**, or **SharedFlow** for communication between Fragment/Activity and ViewModel.
- Prefer `StateFlow` for state management and `SharedFlow` for one-time events.
- Observe data in the Fragment's `viewLifecycleOwner` scope to avoid memory leaks.

### Thread Management
- **Always** use **Coroutines** for asynchronous operations.
- Launch coroutines in the ViewModel using `viewModelScope.launch`.
- Coroutines MUST be tied to the ViewModel lifecycle.

### Presentation Flow
1. Fragment is associated with a ViewModel
2. Fragment calls a method on the ViewModel
3. ViewModel decides which Use Case to execute
4. ViewModel updates the UI state through LiveData/StateFlow

### Naming Conventions
- Fragments: `[Feature]Fragment` (e.g., `LoginFragment`, `ProfileFragment`)
- ViewModels: `[Feature]ViewModel` (e.g., `LoginViewModel`, `ProfileViewModel`)
- UI States: `[Feature]UiState` (e.g., `LoginUiState`)
- UI Events: `[Feature]UiEvent` (e.g., `LoginUiEvent`)

## 🎯 Domain Layer

### Responsibilities
- Contains **business models** (domain entities).
- Contains **Use Cases** (business logic).
- Contains **Repository interfaces** (contracts).

### Clean Architecture Rules
- The Domain layer is **independent** and MUST NOT depend on Presentation or Data layers.
- Communication with other layers happens through **interfaces only**.
- **All business logic** MUST be in Use Cases, never in ViewModels or Data layer.

### Use Cases
- Each Use Case should have a **single responsibility**.
- Use Cases should be invocable classes with an `operator fun invoke()` or `execute()` method.
- Use Cases should return `Result`, `Flow`, or domain models.

### Naming Conventions
- Use Cases: `[Action][Entity]UseCase` (e.g., `GetUserProfileUseCase`, `SaveTaskUseCase`)
- Domain Models: Clear, business-oriented names (e.g., `User`, `Product`, `Order`)
- Repository Interfaces: `[Entity]Repository` (e.g., `UserRepository`, `TaskRepository`)

## 💾 Data Layer

### Responsibilities
- Manages **data sources** logic.
- Handles data retrieval and storage.
- Implements **Repository interfaces** from the Domain layer.

### Repository Implementation
- Repository implementations coordinate between different data sources.
- Repositories decide the data source strategy (cache-first, network-first, etc.).
- Map data models (DTOs/Entities) to domain models.

### Data Sources

#### Local Data Source
- Use **SharedPreferences** for simple key-value storage.
- Use **DataStore** (preferred over SharedPreferences) for type-safe data storage.
- Use **Room Database** for complex local data persistence.
- Implement proper DAOs (Data Access Objects) for Room.

#### Remote Data Source
- Use **Retrofit** for REST API calls.
- Use **Firebase** for real-time databases and cloud services.
- Implement proper API service interfaces.
- Handle network errors and map to domain errors.

### Naming Conventions
- Repository Implementations: `[Entity]RepositoryImpl` (e.g., `UserRepositoryImpl`)
- Data Sources: `[Entity][Type]DataSource` (e.g., `UserRemoteDataSource`, `UserLocalDataSource`)
- DTOs: `[Entity]Dto` or `[Entity]Response` (e.g., `UserDto`, `LoginResponse`)
- Room Entities: `[Entity]Entity` (e.g., `UserEntity`, `TaskEntity`)

## ⚡ Coroutines and Dispatchers

### Context Switching
- Use **`withContext(dispatcher)`** to switch coroutine contexts when performing I/O operations.
- Switch to **`Dispatchers.IO`** for:
    - Network requests (Retrofit calls)
    - Database operations (Room queries)
    - File I/O operations
    - SharedPreferences/DataStore access
- Switch to **`Dispatchers.Default`** for CPU-intensive operations (parsing, calculations).
- UI updates automatically run on **`Dispatchers.Main`** when using `viewModelScope`.

### Dispatcher Injection
- **Inject dispatchers** through constructor parameters for testability.
- Use **Koin** (or Hilt) to provide dispatcher instances.
- This allows replacing dispatchers with `TestDispatchers` in unit tests.

### Example Implementation
```kotlin
// Use Case with injected dispatcher
class GetUserProfileUseCase(
    private val repository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend operator fun invoke(userId: String): Result<User> {
        return withContext(dispatcher) {
            repository.getUserProfile(userId)
        }
    }
}

// Repository with injected dispatcher
class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {
    override suspend fun getUserProfile(userId: String): Result<User> {
        return withContext(dispatcher) {
            try {
                val userDto = remoteDataSource.getUser(userId)
                localDataSource.saveUser(userDto)
                Result.success(userDto.toDomain())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}

// Koin Module
val dispatcherModule = module {
    single<CoroutineDispatcher>(named("IO")) { Dispatchers.IO }
    single<CoroutineDispatcher>(named("Default")) { Dispatchers.Default }
    single<CoroutineDispatcher>(named("Main")) { Dispatchers.Main }
}

val useCaseModule = module {
    factory { GetUserProfileUseCase(get(), get(named("IO"))) }
}

// Test example
class GetUserProfileUseCaseTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    
    @Test
    fun `test use case`() = runTest {
        val useCase = GetUserProfileUseCase(
            repository = mockRepository,
            dispatcher = testDispatcher
        )
        // Test implementation
    }
}
```

### Best Practices
- **Never hardcode** `Dispatchers.IO` or `Dispatchers.Default` directly in classes.
- Always inject dispatchers for better testability.
- Use named qualifiers in Koin (`named("IO")`, `named("Default")`) to distinguish between dispatcher types.
- In tests, use `UnconfinedTestDispatcher` or `StandardTestDispatcher` from `kotlinx-coroutines-test`.

## 🔧 Dependency Injection

- Use **Hilt** (recommended) or **Koin** for dependency injection.
- Provide dependencies at the appropriate scope (Singleton, ViewModelScoped, etc.).
- Repository implementations should be provided as their interfaces.
- ViewModels should receive dependencies through constructor injection.
- **Inject CoroutineDispatchers** for testability and flexibility.

## ⚠️ Error Handling

- Create a sealed class or enum for domain errors.
- Use `Result` or `Either` types to handle success and error cases.
- Never expose raw exceptions to the Presentation layer.
- Map data layer exceptions to domain errors in repositories.

## 🧪 Testing

- Write **unit tests** for ViewModels, Use Cases, and Repositories.
- Use **test doubles** (mocks, fakes) for dependencies.
- Use **Turbine** or similar libraries for testing Flows.
- Use **MockK** or **Mockito** for mocking.
- Repository implementations should have integration tests.
- Replace production dispatchers with **TestDispatchers** (`UnconfinedTestDispatcher`, `StandardTestDispatcher`).
- Use `runTest` from `kotlinx-coroutines-test` for testing suspend functions.

## 📝 Code Quality

- Follow **Kotlin coding conventions**.
- Use **meaningful variable and function names**.
- Keep functions small and focused (Single Responsibility Principle).
- Avoid magic numbers and strings; use constants or resources.
- Use `sealed classes` for representing restricted class hierarchies (UI states, results).
- Prefer `data classes` for models without behavior.
- Use extension functions to improve code readability.

## 🔒 Security

- **Never hardcode** API keys, secrets, or credentials.
- Use `BuildConfig` or secure storage for sensitive data.
- Validate and sanitize user input.
- Use ProGuard/R8 for code obfuscation in release builds.

## 🎨 UI Best Practices

- Use **ViewBinding** or **DataBinding** for view access.
- Follow **Material Design** guidelines.
- Support **different screen sizes** and orientations.
- Implement proper **loading states** and **error messages**.
- Use **ConstraintLayout** for complex layouts.
- Optimize layouts to avoid deep view hierarchies.

## 📦 Resource Management

- Close resources properly (database connections, file streams, etc.).
- Cancel coroutines when no longer needed (handled automatically with `viewModelScope`).
- Unregister listeners and observers when appropriate.
- Use `try-finally` or `use` for resource cleanup.

## 📚 Documentation

- Add **KDoc comments** for public APIs and complex logic.
- Document architectural decisions in code comments when necessary.
- Keep README.md updated with project setup instructions.
- Document any non-obvious business rules or technical decisions.

## 🤖 Copilot Interaction Guidelines

- **Educational Approach**: All comments and suggestions should be educational and adapt to the programmer's skill level.
- **Explain Why**: When suggesting code, briefly explain the reasoning behind architectural or technical decisions.
- **Best Practices**: Always prioritize best practices and maintainable code over quick solutions.
- **Context Awareness**: Understand the current layer (Presentation/Domain/Data) and suggest code that respects architectural boundaries.
- **Progressive Learning**: For junior developers, provide more detailed explanations. For senior developers, focus on advanced patterns and optimizations.

---
