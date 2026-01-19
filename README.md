# Contents

# 🧭 ![Android System Design](https://img.shields.io/badge/Android%20System%20Design-%20-0D6EFD?style=flat&logo=android&logoColor=white) Android System Design for a Small App
A concise, production-oriented guide describing architecture, structure, and rationale for a small-to-medium Android application. Use this as a portfolio README to explain design decisions, onboard[...] 

---

## 🎯 TL;DR
- Recommended architecture: MVVM + Clean Architecture (pragmatic balance for small apps).
- Keep responsibilities separated: UI → ViewModel → Domain (UseCases) → Repository → Data sources.
- Use Hilt for DI, Kotlin coroutines/Flow for async/state, and pure Kotlin domain models for testability.
- Simplify judiciously for very small apps (1–2 screens): skip UseCase layer but never mix UI + data logic.

---

## 📌 Who is this for
- Engineers evaluating architecture choices for a portfolio or starter app.
- New contributors onboarding on a small codebase.
- Interviewers or reviewers wanting a clear rationale and structure.

---

## 🧭 Design goals
- Scalability — easy to add features later without large refactor
- Maintainability — clear separation of concerns and small modules
- Testability — unit tests for business rules and ViewModels without Android framework
- Performance — lightweight, fast startup and predictable lifecycle behavior
- Team readiness — clear conventions so new devs can contribute quickly

---

## 🏗 Recommended architecture
MVVM + Clean Architecture

Why this combination:
- MVVM: lifecycle-aware ViewModel, fits Jetpack, simplifies UI state management.
- Clean Architecture: isolates business rules from framework and data details, enabling testable, portable logic.

High-level flow:
UI (Activity / Fragment / Compose) → ViewModel → UseCase (domain) → Repository → Data source (remote/local/cache)

---

## 🗺 Architecture diagram (textual)
```
[UI] -> [ViewModel] -> [UseCase] -> [Repository] -> { ApiService | Room | InMemoryCache }
```

Notes:
- UseCase layer is optional for very small features but valuable as complexity grows.
- Repositories expose domain models and abstract source selection (remote vs cache).

---

## 📁 Recommended project structure
Use package-by-feature where appropriate; this is a simple package-by-layer example:

```
com.example.app
├── ui
│   └── main
│       ├── MainActivity.kt
│       ├── MainFragment.kt
│       ├── MainViewModel.kt
│       └── MainUiState.kt
├── domain
│   ├── model
│   │   └── User.kt
│   ├── repository
│   │   └── UserRepository.kt
│   └── usecase
│       └── GetUserUseCase.kt
├── data
│   ├── repository
│   │   └── UserRepositoryImpl.kt
│   ├── remote
│   │   └── ApiService.kt
│   ├── local
│   │   └── UserDao.kt
│   └── mapper
│       └── UserMapper.kt
├── di
│   └── AppModule.kt
└── util
    └── Result.kt
```

Prefer feature packages for larger apps:
- com.example.app.feature.profile.ui
- com.example.app.feature.profile.data
- com.example.app.feature.profile.domain

---

## 🧩 Layer responsibilities (concise)

### UI (ui) — Presentational only
- Render state, handle gestures, and forward events to ViewModel.
- Do not contain business rules or data-fetching logic.
Example state:
```kotlin
data class MainUiState(
  val isLoading: Boolean = false,
  val user: User? = null,
  val error: String? = null
)
```

### ViewModel — UI state + orchestration
- Holds UI state (StateFlow / LiveData).
- Calls domain use cases or repositories.
- Survives configuration changes; test with coroutine dispatchers.
Example:
```kotlin
class MainViewModel(
  private val getUserUseCase: GetUserUseCase
): ViewModel() {
  val uiState = MutableStateFlow(MainUiState())

  fun loadUser() {
    viewModelScope.launch {
      uiState.value = uiState.value.copy(isLoading = true)
      runCatching { getUserUseCase() }
        .onSuccess { uiState.value = MainUiState(user = it) }
        .onFailure { uiState.value = MainUiState(error = it.message) }
    }
  }
}
```

### Domain — Business rules (pure Kotlin)
- Models, repository interfaces, and use cases live here.
- No Android framework classes; easy to unit test.
Example model + use case:
```kotlin
data class User(val id: String, val name: String)

interface UserRepository { suspend fun getUser(): User }

class GetUserUseCase(private val repo: UserRepository) {
  suspend operator fun invoke(): User = repo.getUser()
}
```

### Data — Data access & mapping
- Implements repository interfaces, handles DTO ↔ domain mapping, caching, network logic.
Example:
```kotlin
class UserRepositoryImpl(private val api: ApiService): UserRepository {
  override suspend fun getUser(): User = api.getUserDto().toDomain()
}
```

### DI — Dependency wiring
- Use Hilt for small apps. Provide single source of truth for DI modules.
Example:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
  @Provides fun provideUserRepository(api: ApiService): UserRepository =
    UserRepositoryImpl(api)
}
```

---

## ✅ Testing strategy
- Domain: pure unit tests for use cases and mapping logic.
- ViewModel: unit tests using TestCoroutineDispatcher / Turbine for Flow assertions.
- UI: small set of instrumented / Compose tests for critical screens and end-to-end flows.
- Repository: unit tests with fake ApiService / in-memory DB to validate mapping and error cases.

---

## ✂️ When to simplify
If the app is tiny (1–2 screens):
- Skip the UseCase layer, but keep Repository + ViewModel separation.
- Combine DTO-to-domain mapping in repository if it keeps code readable.
- Avoid mixing UI and data logic.

---

## 🧭 Conventions & best practices
- Domain models: immutable data classes, no Android imports.
- Coroutines + Flow: use StateFlow for UI state, expose as StateFlow (immutable) to UI.
- Error handling: return Result or sealed types for success/failure; avoid throwing from ViewModel.
- Mapping: keep mappers in data layer, small and testable.
- Single responsibility: functions & classes should do one thing.

---

## 🧰 Suggested tooling
- Kotlin (latest stable)
- AndroidX Jetpack (ViewModel, Navigation, Lifecycle)
- Hilt (DI)
- Kotlinx.coroutines, Kotlinx.serialization or Moshi/Retrofit
- Room for local persistence
- JUnit, MockK / Mockito, Turbine for Flow testing

---

## 📸 Demo / Screenshots
Include screenshots or short GIFs to demonstrate UX patterns and features. Below is an example screenshot you can include in the repo to showcase the profile screen with a refresh feature. Add the view preview and video preview below:

![App profile update with refresh feature](https://github.com/user-attachments/assets/4534fe20-9459-4f80-8733-7ea3dd5f7a76)

<!-- Embedded video preview using repository file -->

<video controls width="720" preload="metadata" poster="">
  <source src="https://raw.githubusercontent.com/rameshapppro/SystemDesign/main/Screen_recording_20260119_140737.mp4" type="video/mp4">
  Your browser does not support the video tag. You can download the video from the repository: 
  <a href="https://github.com/rameshapppro/SystemDesign/blob/main/Screen_recording_20260119_140737.mp4">Screen_recording_20260119_140737.mp4</a>
</video>


Caption: App profile update with refresh UI state (loading indicator, profile display, pull-to-refresh / refresh button states).

Notes:
- Keep images small and optimized for web.
- Provide alt text and brief captions to describe what the screenshot demonstrates (avoid identifying people in images).

---

## 📁 Portfolio tips (how to present this repo)
- Add this README.md to the root of a sample project to document architecture decisions.
- Link to a small sample app repository that follows this structure.
- Include demo GIFs or screenshots (store them in docs/assets) and reference them from this README.
- Add a short "What I learned" or "Trade-offs considered" section per project to show decision-making.

---

## 📝 Example "Getting started" (add to repo)
```bash
# Clone
git clone https://github.com/<you>/<repo>.git
cd <repo>

# Open in Android Studio and build
# Run unit tests
./gradlew test
# Run instrumented tests
./gradlew connectedAndroidTest
```

---

## ⚖️ Trade-offs & rationale (brief)
- Clean Architecture adds indirection but improves long-term maintainability.
- UseCases increase clarity for business logic; skip them if they add noise for trivial apps.
- Hilt simplifies wiring but introduces learning overhead; replace with manual DI for micro-samples.

---

## Maintained by
**Ramesh A.**  
**Tech Lead Engineer | Android Architect**  

GitHub: https://github.com/rameshapppro  
Last updated: 18 January 2026
