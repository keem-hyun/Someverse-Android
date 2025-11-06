# Clean Architecture Rules

## Layer Structure

```
presentation (UI Layer)
    ↓
domain (Business Logic Layer)
    ↑
data (Data Layer)
```

Dependency direction: `presentation → domain ← data`

## Core Principles

### 1. Dependency Rule

- **Outer layers can depend on inner layers, but inner layers must not know about outer layers**
- Dependency direction: `presentation → domain ← data`
- Domain layer must not know about any other layers

### 2. Domain Layer (Core Business Logic)

#### What to Include:

- ✅ UseCase (Business logic)
- ✅ Entity (Domain model)
- ✅ Repository Interface (Implementation in data layer)
- ✅ Pure Kotlin code only

#### Prohibited:

- ❌ Android Framework dependencies (Context, Activity, Fragment, etc.)
- ❌ External library dependencies (Retrofit, Room, etc.)
- ❌ Importing data or presentation layers
- ❌ UI-related code

### 3. Data Layer (Data Processing)

#### What to Include:

- ✅ Repository Implementation
- ✅ DataSource (Remote, Local)
- ✅ DTO (Data Transfer Object)
- ✅ Mapper (DTO ↔ Domain Model conversion)
- ✅ API services, Database access code

#### Rules:

- Implement Domain's Repository Interface
- Convert and return Domain Model
- Do not import presentation layer

### 4. Presentation Layer (UI)

#### What to Include:

- ✅ Activity, Fragment, Composable
- ✅ ViewModel
- ✅ UI State, UI Event
- ✅ Mapper (Domain Model → UI Model, only when needed)

#### Rules:

- Depend only on Domain's UseCase
- Do not directly reference data layer
- Do not include business logic (prohibited even in ViewModel)

## Data Flow

### Data Query Flow

```
UI → ViewModel → UseCase → Repository Interface (domain)
                              ↓
                    Repository Impl (data) → DataSource → API/DB
                              ↓
                    Mapper: DTO → Domain Model
                              ↓
UI ← ViewModel ← UseCase ← Domain Model
```

### Data Save Flow

```
UI → ViewModel → UseCase → Repository Interface (domain)
                              ↓
                    Repository Impl (data) → Mapper: Domain Model → DTO
                              ↓
                    DataSource → API/DB
```

## Package Structure Example

```
com.example.someverse/
├── domain/
│   ├── model/
│   │   └── User.kt
│   ├── repository/
│   │   └── UserRepository.kt
│   └── usecase/
│       └── GetUserUseCase.kt
├── data/
│   ├── remote/
│   │   ├── api/
│   │   │   └── UserApi.kt
│   │   └── dto/
│   │       └── UserDto.kt
│   ├── local/
│   │   ├── dao/
│   │   └── entity/
│   ├── mapper/
│   │   └── UserMapper.kt
│   └── repository/
│       └── UserRepositoryImpl.kt
└── presentation/
    ├── ui/
    │   └── user/
    │       ├── UserScreen.kt
    │       └── UserViewModel.kt
    └── model/
        └── UserUiModel.kt
```