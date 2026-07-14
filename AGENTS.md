# Now in Android Agent Guide

Use this file as the working contract for AI coding agents in this repository. The project is a
modern, modular Android application written in Kotlin. It follows Google's Android architecture
guidance: reactive UI, unidirectional data flow, repository-based data access, dependency injection,
and clear module boundaries.

## Project Snapshot

- App: a single-activity native Android app that shows Android development news, topics, bookmarks,
  search, settings, and notifications.
- UI: Jetpack Compose, Material 3, adaptive layouts, Navigation 3, and window size classes.
- State: ViewModels expose immutable UI state through Kotlin Coroutines and `Flow`.
- DI: Hilt is used across app, feature, sync, data, database, and test code.
- Data: repositories coordinate Room, DataStore, network APIs, and sync state.
- Network: Retrofit, OkHttp, Kotlin serialization, and flavor-specific implementations.
- Background work: WorkManager handles deferrable sync and notification-related work.
- Build: Gradle Kotlin DSL with version catalogs and local convention plugins in `build-logic`.
- Minimum Java: JDK 17 or newer.

## Repository Layout

- `app/`: main Android application, `MainActivity`, app-level navigation host, Firebase, baseline
  profile wiring, and dependency guard configuration.
- `app-nia-catalog/`: design system/catalog app for previewing UI components.
- `feature/<name>/api/`: public feature contracts such as navigation entry points.
- `feature/<name>/impl/`: feature UI, ViewModels, feature-specific state, and implementation
  dependencies. Current features include `foryou`, `interests`, `bookmarks`, `topic`, `search`, and
  `settings`.
- `core/common/`: shared coroutine, result, dispatcher, and utility code.
- `core/model/`: pure model types shared across layers.
- `core/data/`: repositories and data synchronization coordination.
- `core/database/`: Room database, DAOs, entities, and database mapping.
- `core/datastore/` and `core/datastore-proto/`: DataStore storage and protobuf schemas.
- `core/network/`: Retrofit services, network DTOs, serialization, and data source abstractions.
- `core/domain/`: use cases that combine repositories for feature-facing operations.
- `core/ui/`: shared Compose UI patterns that are not part of the design system.
- `core/designsystem/`: reusable Material 3 components, icons, theme, and previews.
- `core/navigation/`: shared navigation types and helpers.
- `core/analytics/`, `core/notifications/`: analytics and notification abstractions.
- `core/testing/`, `core:data-test`, `core:datastore-test`, `sync:sync-test`: test doubles,
  fixtures, runners, and fake implementations.
- `sync/work/`: WorkManager sync implementation.
- `benchmarks/`: macrobenchmarks and baseline profile generation.
- `lint/`: custom lint checks.
- `build-logic/`: included Gradle build containing composable convention plugins.
- `gradle/libs.versions.toml`: the source of truth for dependency and plugin versions.

## Build Logic And Dependencies

- Prefer existing convention plugins over duplicating Gradle setup. Common plugins include
  `nowinandroid.android.application`, `nowinandroid.android.library`,
  `nowinandroid.android.library.compose`, `nowinandroid.android.feature.api`,
  `nowinandroid.android.feature.impl`, `nowinandroid.android.room`, and `nowinandroid.hilt`.
- Add new dependency versions to `gradle/libs.versions.toml`; do not hardcode versions in module
  build files.
- Root `settings.gradle.kts` enables type-safe project accessors. Use `projects.core.data` style
  project dependencies.
- Keep convention plugins additive and reusable. Put one-off module behavior in that module's
  `build.gradle.kts` instead of expanding build logic prematurely.
- The app uses `demo` and `prod` product flavors plus `debug`, `release`, and benchmark-related
  build types. Be explicit about the variant when running Gradle tasks.

## Architecture Rules

- Preserve module boundaries. Feature implementation modules may depend on their own API module,
  other feature API modules, and appropriate `core` modules; avoid feature-implementation to
  feature-implementation coupling unless the existing graph already establishes it.
- Keep API modules small. Put contracts, navigation surface, and public types there; keep UI and
  implementation details in `impl`.
- Use unidirectional data flow: UI events go to ViewModels or state holders, state flows back as
  immutable UI state.
- Keep Compose functions mostly stateless. Use route/container composables for ViewModel wiring and
  screen/content composables for rendering state.
- Collect flows in Compose using lifecycle-aware APIs already used by the project.
- Use repositories for data access. Do not fetch directly from network, Room, or DataStore inside
  UI or ViewModels.
- Put cross-repository business operations in `core/domain` use cases when they are shared or
  non-trivial.
- Inject dispatchers, repositories, DAOs, services, and workers with Hilt rather than constructing
  them in consumers.
- Keep model transformations close to layer boundaries: network DTOs, database entities, and domain
  models should remain distinct when the surrounding code already separates them.

## Kotlin And Compose Style

- Follow the existing Kotlin style, package structure, and naming in nearby files before adding new
  patterns.
- Prefer immutable data classes, sealed interfaces/classes for UI state, and explicit loading/error
  states where existing screens use them.
- Keep public APIs minimal and stable. Avoid exposing implementation-specific types from `api`
  modules.
- Use Material 3 and existing `core/designsystem` components before adding new UI primitives.
- Add previews for reusable design system or shared UI components when nearby components have
  previews.
- Use `stringResource`, plural resources, and existing resource conventions for user-visible text.
- Avoid introducing new architectural libraries or UI frameworks unless the task explicitly requires
  it and the tradeoff is clear.

## Data, Sync, And Storage

- Room changes belong in `core/database`; include schema/entity/DAO updates and focused tests where
  behavior changes.
- DataStore and protobuf changes belong in `core/datastore` or `core/datastore-proto`; preserve
  backward-compatible proto numbering.
- Network DTOs and Retrofit service changes belong in `core/network`; keep serialization explicit.
- Repository behavior belongs in `core/data`; expose flows and suspend functions that match existing
  repository conventions.
- WorkManager sync behavior belongs in `sync/work`; keep worker dependencies injected and testable.

## Testing Guidance

- Prefer focused local tests for ViewModels, repositories, use cases, mappers, and sync behavior.
- Use `kotlinx.coroutines.test` for coroutine code, Turbine for complex Flow assertions, and Truth
  or Kotlin test assertions matching nearby tests.
- UI feature tests should use Compose testing APIs with a `ComponentActivity` unless the existing
  test pattern for that feature says otherwise.
- App-level end-to-end tests live in `app/` and may launch `MainActivity`.
- Screenshot tests use Roborazzi. CI generates screenshot artifacts, so do not check generated
  screenshots into the repo from a workstation.
- Instrumented tests use Gradle-managed devices where possible.
- Add or update tests in the same module as the changed behavior unless the behavior is explicitly
  integration-level.

## Common Commands

- Build demo debug: `./gradlew assembleDemoDebug`
- Build prod release: `./gradlew assembleProdRelease`
- Run all local tests for a variant: `./gradlew demoDebugTest`
- Run a single local test: `./gradlew demoDebugTest --tests "com.example.MyTest"`
- Run screenshot tests: `./gradlew verifyRoborazziDemoDebug`
- Update screenshot baselines only when intentionally requested: `./gradlew recordRoborazziDemoDebug`
- Run managed-device tests: `./gradlew pixel6api31aospDebugAndroidTest`
- Other managed devices: `./gradlew pixel4api30aospatdDebugAndroidTest` and
  `./gradlew pixelcapi30aospatdDebugAndroidTest`
- Format and apply lint style fixes: `./gradlew spotlessApply`
- Check formatting: `./gradlew spotlessCheck`
- Run custom lint/checks for touched modules when relevant: `./gradlew lintDemoDebug`

## Agent Workflow

- Inspect nearby code and module build files before editing. Let existing patterns win.
- Keep changes scoped to the requested behavior and the owning module.
- Prefer small, composable changes over broad refactors. Do not move code across modules unless the
  task requires it.
- Update Gradle dependencies through the version catalog and existing convention plugins.
- Run the narrowest useful verification first, then broader checks when the change crosses module
  boundaries.
- Do not revert unrelated local changes. Treat unrecognized worktree changes as user-owned.
- Do not commit generated screenshots, build outputs, `.gradle`, or IDE metadata.
- When adding a new module, include it in `settings.gradle.kts`, choose the closest convention
  plugin, set an explicit namespace, and wire dependencies through type-safe project accessors.
- When adding a new feature, prefer the existing `feature:<name>:api` and `feature:<name>:impl`
  split unless the feature is intentionally internal-only.

## CI And Release Notes

- CI configuration lives in `.github/workflows/*.yaml` and Kokoro config lives in `kokoro/`.
- Release builds may use R8 minification and baseline profile generation.
- Dependency guard tracks selected runtime classpaths. If dependency output changes intentionally,
  update the corresponding guard file with a clear reason.

## Version Control

- The upstream project is hosted at `https://github.com/android/nowinandroid`.
- Keep commits focused and explain user-visible behavior, tests run, and any skipped verification.
