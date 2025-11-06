# Multi-Module Architecture

## Layer and Module Relationship

```
someverse/
├── app/                          # Application module
│   └── AndroidManifest.xml
│   └── Application class
│   └── MainActivity
├── feature/                      # Feature modules
│   ├── feature-auth/            # Auth feature (login, signup)
│   ├── feature-home/            # Home screen
│   ├── feature-profile/         # Profile
│   ├── feature-setting/         # Settings
│   └── feature-onboarding/      # Onboarding
├── core/                         # Core modules
│   ├── core-ui/                 # Common UI components
│   ├── core-designsystem/       # Design system (theme, color, typography)
│   ├── core-navigation/         # Navigation
│   ├── core-network/            # Network configuration (Retrofit, OkHttp)
│   ├── core-data/               # Common data logic
│   └── core-common/             # Common utilities
├── domain/                       # Domain modules
│   ├── domain-auth/
│   ├── domain-user/
│   └── domain-common/
└── data/                         # Data modules
    ├── data-auth/
    ├── data-user/
    └── data-local/
```

## Module Dependency Rules

```
           app
            ↓
    ┌───────┴───────┐
    ↓               ↓
feature-*       data-*
    ↓               ↓
    ↓          domain-*
    ↓               ↑
    └──→ core-* ←───┘
```

### Dependency Flow

1. **app** → all feature modules
2. **feature** → domain, core
3. **data** → domain, core-network, core-data
4. **domain** → depends on nothing (pure Kotlin)
5. **core** → other core modules (when needed)

## Detailed Module Type Description

### 1. app module

**Role:**

- Application entry point
- DI configuration (Hilt)
- Navigation graph integration
- Integrates all feature modules

**Dependencies:**

- All feature modules
- data modules (for DI binding)
- core modules

**Contains:**

- Application class (@HiltAndroidApp)
- MainActivity
- Overall navigation graph

### 2. feature module (Android Library)

**Role:**

- Independent feature unit
- UI, ViewModel, UI State
- Completely independent per feature

**Dependencies:**

- domain module (UseCase, Model for the feature)
- core-ui, core-designsystem, core-navigation
- Other feature modules (absolutely prohibited)
- data module (no direct dependency)

**Structure:**
```
feature-user/
├── src/main/java/com/example/someverse/feature/user/
│   ├── UserScreen.kt
│   ├── UserViewModel.kt
│   ├── UserUiState.kt
│   ├── UserUiEvent.kt
│   ├── navigation/
│   │   └── UserNavigation.kt
│   └── component/
│       └── UserCard.kt
```

**Navigation Pattern:**

- Provide navigation as NavGraphBuilder extension function
- Define routes as Destination object
- Expose for use by other features

### 3. domain module (Kotlin/Java Library)

**Role:**

- Business logic
- UseCase, Entity, Repository Interface
- Pure Kotlin code

**Dependencies:**

- Depends on nothing
- No Android Framework
- No external libraries (except Coroutines)

**Structure:**
```
domain-user/
├── src/main/java/com/example/someverse/domain/user/
│   ├── model/
│   │   └── User.kt
│   ├── repository/
│   │   └── UserRepository.kt
│   └── usecase/
│       ├── GetUserUseCase.kt
│       ├── UpdateUserUseCase.kt
│       └── DeleteUserUseCase.kt
```

**Important:**

- No Android Framework classes (Context, Activity, etc.)
- Repository defined as Interface only
- UseCase implements operator fun invoke
- Use @Inject constructor for dependency injection

### 4. data module (Android Library)

**Role:**

- Repository implementation
- DataSource (Remote, Local)
- DTO, Entity, Mapper

**Dependencies:**

- domain module (implements Repository Interface)
- core-network, core-data
- feature modules (prohibited)
- presentation layer (prohibited)

**Structure:**
```
data-user/
├── src/main/java/com/example/someverse/data/user/
│   ├── repository/
│   │   └── UserRepositoryImpl.kt
│   ├── remote/
│   │   ├── api/
│   │   │   └── UserApi.kt
│   │   ├── dto/
│   │   │   └── UserDto.kt
│   │   └── datasource/
│   │       ├── UserRemoteDataSource.kt
│   │       └── UserRemoteDataSourceImpl.kt
│   ├── local/
│   │   ├── dao/
│   │   │   └── UserDao.kt
│   │   ├── entity/
│   │   │   └── UserEntity.kt
│   │   └── datasource/
│   │       ├── UserLocalDataSource.kt
│   │       └── UserLocalDataSourceImpl.kt
│   ├── mapper/
│   │   ├── UserDtoMapper.kt
│   │   └── UserEntityMapper.kt
│   └── di/
│       └── UserDataModule.kt
```

### 5. core modules (Android Library)

#### core-ui

**Contains:**

- Common Composable components
- Extension functions
- Custom Modifiers

#### core-designsystem

**Contains:**
- Theme
- Color
- Typography
- Spacing
- Shape

#### core-navigation

**Contains:**

- Navigation-related common code
- Route definitions
- Navigator interface

#### core-network

**Contains:**

- Retrofit configuration
- OkHttp configuration
- Interceptors
- API error handling

#### core-data

**Contains:**

- Room Database configuration
- DataStore configuration
- Common data-related utilities

#### core-common

**Contains:**

- Common utilities
- Extension functions
- Constants

## build.gradle.kts Configuration by Module

### domain module
- Plugin: `java-library`, `kotlin.jvm`
- Java Version: 17
- Dependencies: Coroutines, javax.inject, test libraries only

### data module
- Plugin: `android.library`, `kotlin.android`, `hilt`, `serialization`, `kapt`
- Dependencies: domain module, core modules, Hilt, Retrofit, Room

### feature module
- Plugin: `android.library`, `kotlin.android`, `hilt`, `kapt`
- Dependencies: domain module, core modules, Hilt, Compose, Navigation
- Compose must be enabled

### core module
- Plugin: `android.library`, `kotlin.android`, `hilt`, `kapt`
- Dependencies: Varies by purpose
- Expose common libraries with `api` (Retrofit, OkHttp, etc.)

### app module
- Plugin: `android.application`, `kotlin.android`, `hilt`, `kapt`
- Dependencies: All feature, data, core modules
- Set applicationId, versionCode, versionName

## settings.gradle.kts Configuration

Register all modules with include:
- `:app`
- `:feature:feature-*`
- `:domain:domain-*`
- `:data:data-*`
- `:core:core-*`

## Module Creation Order

1. **Create core modules** (dependencies for other modules)
   - core-common
   - core-network
   - core-data
   - core-designsystem
   - core-ui
   - core-navigation

2. **Create domain modules** (business logic)
   - domain-common
   - domain-auth
   - domain-user

3. **Create data modules** (data processing)
   - data-local
   - data-auth
   - data-user

4. **Create feature modules** (UI)
   - feature-auth
   - feature-home
   - feature-profile
   - feature-setting