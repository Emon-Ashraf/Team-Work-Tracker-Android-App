<div align="center">

# Team Work Tracker

**An Android client for organizing teams, projects, and assigned tasks.**

Kotlin · Jetpack Compose · Material 3 · Retrofit

[Overview](#overview) · [Features](#implemented-features) · [Screenshots](#screenshots) · [Getting started](#getting-started) · [API](#backend-and-api) · [Development status](#development-status)

</div>

## Overview

Team Work Tracker brings personal task tracking and team collaboration into a single Android application. Users can sign in, review their assigned work, create or join teams, and organize projects within those teams. Tasks support priorities, statuses, due dates, comments, and attachments.

The project demonstrates a Compose UI, ViewModel-driven state, repository abstractions, and REST API integration. It is under development: several flows communicate with a backend, while registration and project details still have incomplete integration. The feature descriptions below reflect the checked-in Android implementation; live backend behavior has not been verified.

## Implemented features

| Area | Implemented Android functionality |
| --- | --- |
| Sign-in | Email/password login through the API; access token persistence in Preferences DataStore; bearer authentication on subsequent requests. |
| Dashboard | Links to My Tasks and Teams; counts for total, new, in-progress, and high-priority assigned tasks; up to three tasks sorted by due-date string; manual refresh. |
| Teams | List the signed-in user's teams, create a team, join by numeric team ID, and view team metadata and members. |
| Projects | List projects for a selected team and create a project with a name and description through the API. The list is filtered by team on the client. |
| Tasks | List assigned tasks; view, create, edit, and delete tasks, with a confirmation dialog before deletion. |
| Task attributes | Title, description, project ID, assignee ID, due date, three priorities, and four statuses. |
| Comments | Fetch task comments and submit a new text comment. |
| Attachments | Fetch attachment metadata, add a URL, and select a document with Android's system picker for multipart upload. |
| UI development | Separate screen content composables and deterministic fixtures for Android Studio previews, with light and dark preview configurations. Screens use a custom dark palette. |

**Task priorities:** `low`, `medium`, `high`

**Task statuses:** `new`, `in_progress`, `completed`, `blocked`

Task details refresh when the screen resumes. Task data, comments, and attachments are fetched concurrently in its ViewModel. Attachment URLs are displayed as text; an attachment viewer or download action is not implemented.

## Screenshots

Application screenshots are not currently included in this repository. The expected `docs/screenshots/` directory was absent when the documentation was prepared, so no gallery or image links are published here.

Compose previews are available in the screen source files for inspecting the UI in Android Studio. They use sample data and are not captured application screenshots.

## Technology stack

Versions below come from the dependencies selected in [app/build.gradle.kts](app/build.gradle.kts) and the [version catalog](gradle/libs.versions.toml).

| Technology | Version / configuration | Role |
| --- | --- | --- |
| Kotlin | 2.0.0 | Application code and Compose compiler plugin |
| Jetpack Compose | BOM 2024.04.01 | Declarative UI, tooling, and previews |
| Material 3 / extended Material icons | Managed by Compose BOM | UI components and icons |
| Activity Compose | 1.10.1 | Activity integration |
| AndroidX Core KTX | 1.16.0 | Android Kotlin extensions |
| Lifecycle Runtime KTX | 2.9.1 | Lifecycle support |
| Lifecycle ViewModel Compose | 2.10.0 | ViewModels in Compose |
| Navigation Compose | 2.9.6 | Navigation graph and parameterized routes |
| Preferences DataStore | 1.2.0 | Access-token storage |
| Retrofit / Gson converter | 2.11.0 | REST API interfaces and JSON conversion |
| OkHttp / logging interceptor | 4.12.0 | HTTP client, bearer interception, and request logging |
| Kotlin coroutines / Flow | Used through the dependency graph; no explicit app-level version | Asynchronous work and observable UI state |
| JUnit | 4.13.2 | Local test dependency |
| AndroidX JUnit / Espresso | 1.2.1 / 3.6.1 | Instrumentation test dependencies |
| Compose UI test JUnit4 | Managed by Compose BOM | Compose test dependency |

The catalog also declares Retrofit/Gson 3.0.0 and OkHttp/logging 5.3.2 aliases, but the app selects the **2.11.0** and **4.12.0** aliases listed above.

### Build configuration

| Setting | Checked-in value |
| --- | --- |
| Android Gradle Plugin | 8.8.2 |
| Gradle wrapper | 8.14.6 |
| Compile SDK | 36 |
| Target SDK | 35 |
| Minimum SDK | 24 — Android 7.0 |
| Java source/target and Kotlin JVM target | 11 |
| Gradle daemon JVM criteria | Microsoft JDK 21 |
| Application ID / namespace | `com.example.teamworktracker` |
| App version | `1.0` (`versionCode = 1`) |
| Modules | One Android application module: `:app` |

These are declared settings, not a guarantee that the current combination builds successfully on every toolchain. Java/Kotlin bytecode target 11 is separate from the JDK used to run Gradle.

## Architecture

The app uses an **MVVM-style presentation layer with repositories** in a single module. It has logical package boundaries rather than separate Gradle modules or a complete Clean Architecture use-case layer.

```text
Compose screen / screen content
        │ actions              ▲ observable state
        ▼                      │
     ViewModel ── coroutine / StateFlow
        │
        ▼
     Repository
        │
        ▼
Retrofit API ── OkHttp ── REST backend
                   │
            AuthInterceptor
                   │
       SessionManager / DataStore
```

- **Presentation:** feature packages contain screens, reusable cards, and ViewModels. Most asynchronous screens expose `StateFlow` state for loading, data, errors, or action completion; form fields also use Compose state.
- **Data access:** `TaskRepository` and `TeamRepository` define contracts implemented by remote repositories. Authentication and projects use concrete repository classes.
- **Networking:** `ApiClient` initializes Retrofit in `MainActivity`, configures Gson's snake_case field naming, and attaches authentication and logging interceptors. Task DTOs are mapped to domain models; some other API responses use domain models directly.
- **Session:** `SessionManager` exposes the stored access token as a Flow. `AuthInterceptor` reads it and adds the `Authorization: Bearer <token>` header when available.
- **Dependency construction:** repository/API dependencies are created manually, often through default constructor arguments. No dependency injection framework is configured.
- **Current exceptions:** comment and attachment ViewModels call Retrofit interfaces directly. `ProjectDetailsScreen` reads `TestProjectRepository` and `TestTaskRepository` at runtime.

### Navigation

[AppNavHost.kt](app/src/main/java/com/example/teamworktracker/ui_screens/navigation/AppNavHost.kt) defines the routes, with numeric IDs passed as navigation arguments.

```text
Login ↔ Register
  └─ Home
      ├─ My Tasks → Create Task
      │    └─ Task Details → Edit Task / Add Comment / Add Link / Add File
      └─ Teams → Create Team / Join Team
           └─ Team Details → Projects → Create Project
                                  └─ Project Details → Task Details
```

The graph starts at Login on every launch. A saved token does not currently change the start destination. The Register route can navigate to Home after local validation, but it does not create an account or obtain a token.

## Project structure

```text
Team-Work-Tracker-Android-App/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/example/teamworktracker/
│       │   │   ├── MainActivity.kt
│       │   │   ├── core/session/       # DataStore-backed session manager
│       │   │   ├── data/               # Repository contracts and remote access
│       │   │   │   └── testRepo/       # Hard-coded sample repositories
│       │   │   ├── domain/models/      # Tasks, teams, projects, comments, attachments
│       │   │   ├── network/
│       │   │   │   ├── api/            # Retrofit interfaces and request models
│       │   │   │   └── dto/            # Transfer objects and task mappers
│       │   │   ├── ui/theme/           # Colors, typography, and Material theme
│       │   │   └── ui_screens/
│       │   │       ├── auth/
│       │   │       ├── home/
│       │   │       ├── navigation/
│       │   │       ├── preview/        # Compose preview fixtures
│       │   │       ├── project/
│       │   │       ├── tasks/
│       │   │       └── team/
│       │   └── res/                    # Icons, strings, themes, backup rules
│       ├── test/                       # Example local unit test
│       └── androidTest/                # Example instrumentation test
├── gradle/
│   ├── libs.versions.toml
│   ├── gradle-daemon-jvm.properties
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
└── gradlew.bat
```

## Getting started

### Prerequisites

- Git and Android Studio with support for the project's Android Gradle Plugin and Kotlin configuration.
- Android SDK Platform 36 and the SDK tools requested during Gradle sync.
- A Microsoft JDK 21 installation available to Gradle, matching [gradle/gradle-daemon-jvm.properties](gradle/gradle-daemon-jvm.properties). The project applies the Foojay toolchain resolver; automatic provisioning depends on the local Gradle/toolchain setup.
- An Android emulator or device running API 24 or later.
- Network access to download Gradle and Maven dependencies.
- For API-backed use: a compatible running backend, an existing user account, and appropriate access to team/project records.

### Open the project

```bash
git clone https://github.com/Emon-Ashraf/Team-Work-Tracker-Android-App.git
cd Team-Work-Tracker-Android-App
```

Open the repository root in Android Studio, select the appropriate Gradle JDK, and sync the project. Let Android Studio configure the local SDK path in the ignored `local.properties` file if needed.

### Build and run

From the repository root on Linux/macOS:

```bash
bash ./gradlew :app:assembleDebug
```

The wrapper is tracked without the executable bit, so invoking it with `bash` avoids changing file permissions. In Windows PowerShell:

```powershell
.\gradlew.bat :app:assembleDebug
```

After a successful build, the debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

To launch from Android Studio, select the `app` run configuration and an emulator or connected device, then choose **Run**. To install the debug build from Linux/macOS with a device connected:

```bash
bash ./gradlew :app:installDebug
```

For Windows, use `.\gradlew.bat` in place of `bash ./gradlew` for the equivalent tasks.

### Available verification tasks

```bash
bash ./gradlew :app:testDebugUnitTest
bash ./gradlew :app:lintDebug
bash ./gradlew :app:connectedDebugAndroidTest
```

The last command requires a connected device or running emulator. The current test source contains only an arithmetic unit test and an application-package instrumentation check; these do not verify the application flows or backend integration. Compose test libraries are declared, but there is no feature-level Compose test suite.

## Backend and API

This repository contains the Android client. Backend source code, deployment instructions, seed accounts, and a backend repository link are not included; the server framework and database cannot be established from this client alone.

### Base URL and connectivity

The base URL is a source constant in [network/ApiConstants.kt](app/src/main/java/com/example/teamworktracker/network/ApiConstants.kt). It currently points to a private LAN host on port `8000` over HTTP. It is not a public demo service, and there is no environment variable, `local.properties` key, build flavor, or settings screen for overriding it.

To connect your own compatible backend, a local source configuration change to `ApiConstants.BASE_URL` is required. Keep the trailing `/` required by Retrofit. For example, **only when your backend runs on the emulator host on port 8000**, the standard Android emulator can use `http://10.0.2.2:8000/`. A physical device needs an address reachable from that device; the backend must listen on an accessible interface and the firewall must allow the connection. `localhost` on a device refers to the device itself.

The manifest already declares Internet permission and permits cleartext traffic. No API key is configured in the client. Use an existing backend account to sign in; the registration screen cannot provision one.

### Client API contract

Paths below are declared by the Retrofit interfaces under [network/api](app/src/main/java/com/example/teamworktracker/network/api). They describe client expectations, not a separately validated server specification.

| Resource | Methods and paths | Integration |
| --- | --- | --- |
| Authentication | `POST /api/v1/auth/login` | JSON email/password login; response contains `access_token` and `token_type`. |
| Current user | `GET /api/v1/users/me` | Interface exists; no profile screen or active UI call. |
| Teams | `GET /api/v1/teams/my-teams`; `GET` / `POST /api/v1/teams/` | User's teams and team creation are wired; the all-teams query is not exposed by a browsing screen. |
| Team details | `GET /api/v1/teams/{team_id}`; `GET /api/v1/teams/{team_id}/members`; `POST /api/v1/teams/join` | Details, members, and joining with a JSON `team_id`. |
| Projects | `GET` / `POST /api/v1/projects/`; `GET /api/v1/projects/{project_id}` | List/create are wired. The detail endpoint exists but the detail screen still uses sample data. |
| Tasks | `GET /api/v1/tasks/my-tasks`; `POST /api/v1/tasks/`; `GET` / `PUT` / `DELETE /api/v1/tasks/{task_id}` | Assigned list and task CRUD. |
| Comments | `GET` / `POST /api/v1/tasks/{taskId}/comments` | Read comments; submit JSON `content`. |
| Attachments | `GET /api/v1/tasks/{taskId}/attachments`; `POST /api/v1/tasks/{taskId}/attachments/link`; `POST /api/v1/tasks/{taskId}/attachments/file` | List, JSON link submission, and multipart file upload. |

Task requests use `project_id`, `assigned_to`, `priority`, `status`, and `due_date` alongside title and description. Creation requires numeric project and assignee IDs entered manually. Due dates are entered as text, with ISO-style values recommended by the UI; there is no date picker. File upload sends a multipart `file` part and an optional `description` part.

### Session and network considerations

Access tokens are stored in Preferences DataStore. Token refresh, automatic handling of expired sessions, and a logout UI are not implemented. The HTTP logging interceptor is currently configured at `BODY` level without a debug-only guard; review logging and transport configuration before using sensitive accounts or distributing a production build.

## Development status

**Status: in development, with partial backend integration.**

| Area | Current limitation |
| --- | --- |
| Registration | Validates non-empty fields and a minimum password length, then navigates to Home. It makes no registration request and saves no new token. |
| Project details | Uses hard-coded sample projects and tasks. A real project may display unrelated sample data if IDs overlap, or show “Project not found.” Opening a sample task still navigates to the API-backed task detail route. |
| Team task summary | The team detail screen contains a placeholder instead of a team task list. |
| Dashboard dates | “Today overview” summarizes all returned assigned tasks rather than filtering to today's date. Upcoming tasks are ordered by due-date strings, including completed tasks. |
| Data and lists | No local task/project database or offline sync is configured. Project listing fetches the default first page of up to 100 projects and filters it locally; pagination is not exposed in the UI. |
| File handling | Upload reads the complete file into memory and sends the fixed filename `upload_file`; there is no upload progress indicator or attachment viewer/download flow. |
| Profile and sessions | A current-user API interface and session-clearing method exist, but profile, logout, and automatic session restoration navigation are not wired. |
| Verification | Only template tests are present. A successful build and live backend operation have not been established by this documentation update. |

## Suggested future improvements

These are suggestions, not implemented features or delivery commitments:

- Connect registration and project details to real backend data, and replace the team task placeholder.
- Add session-aware startup, logout, and expired-token handling.
- Replace manual IDs with project/member selectors and add a date picker.
- Add pagination, search/filter controls, and local caching where useful.
- Stream uploads, preserve original filenames, and support opening attachments.
- Separate debug/release endpoint and logging configuration, use HTTPS, and validate dependency/toolchain compatibility in CI.
- Add repository, ViewModel, navigation, and Compose UI tests for the primary workflows.
- Include verified application screenshots in `docs/screenshots/` and publish a grouped gallery.

## Author

Maintained by [Emon Ashraf (@Emon-Ashraf)](https://github.com/Emon-Ashraf).

**Repository:** [Emon-Ashraf/Team-Work-Tracker-Android-App](https://github.com/Emon-Ashraf/Team-Work-Tracker-Android-App)
