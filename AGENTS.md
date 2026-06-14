# Repository Guidelines

This repository hosts **Legado (开源阅读)**, a free, open-source Android novel reader backed by a Vue 3 web companion for bookshelf and source editing. The Android app is the primary deliverable; the web module is a separate front-end bundled into the APK. Use this guide to set up your environment and contribute consistently.

## Project Structure & Module Organization

- `app/` — Android application module (Kotlin, AGP). Main source lives under `app/src/main/java/io/legado/app/`, grouped by feature: `ui/`, `data/` (`dao/`, `entities/`), `api/`, `service/`, `web/`, `utils/`, `lib/`, `base/`, `model/`, `help/`, `constant/`, `exception/`, `receiver/`.
- `app/src/main/res/` — resources (layouts, drawables, localized strings, mipmaps, raw assets). Localized variants live in `values-*` folders.
- `app/src/main/assets/` — bundled defaults, fonts, epub templates, and the embedded web UI in `assets/web/`.
- `app/src/test/` and `app/src/androidTest/` — JVM unit tests and instrumented tests. Room schemas export to `app/schemas/`.
- `modules/book/`, `modules/rhino/` — Android library modules included via `settings.gradle`. Treat them as part of the app build.
- `modules/web/` — Vue 3 + Vite + TypeScript SPA. Source in `modules/web/src/{api,assets,components,config,hooks,pages,plugins,router,store,utils,views}`.
- `gradle/libs.versions.toml` — version catalog for every dependency and plugin.

## Build, Test, and Development Commands

Run from the repository root unless noted.

- `./gradlew assembleAppDebug` — build the Android app (debug). Use `assembleAppRelease` for release builds.
- `./gradlew :app:test` — run JVM unit tests.
- `./gradlew :app:connectedAppDebugAndroidTest` — run instrumented tests on a connected device or emulator.
- `./gradlew lintAppDebug` — run Android Lint for the debug variant.
- `./gradlew clean` — delete the root `build/` directory.
- `cd modules/web && pnpm install` — install web dependencies (Node ≥ 20, pnpm ≥ 9).
- `pnpm dev` — start the Vite dev server (default `http://localhost:8080`, requires the Legado app backend on the device).
- `pnpm build`, `pnpm type-check`, `pnpm lint:fix`, `pnpm format` — web build, Vue/TS type check, ESLint auto-fix, and Prettier formatting.

## Coding Style & Naming Conventions

- Kotlin code follows standard Android Studio formatting (4-space indent, trailing newline). Java compatibility and the Kotlin toolchain are pinned to Java 17 in `app/build.gradle`.
- Package root: `top.overlink.read.*`. Use `PascalCase` for classes, `lowerCamelCase` for functions and properties, and `UPPER_SNAKE_CASE` for constants.
- Vue/TS code (in `modules/web/`) uses 2-space indent, single quotes, no semicolons, and trailing commas where required (see `modules/web/.editorconfig` and `.prettierrc.json`).
- Web linting/formatting: ESLint via `modules/web/eslint.config.mjs` and Prettier. Run `pnpm lint:fix` and `pnpm format` before committing web changes.
- Do not edit the commented-out mirror repositories in `settings.gradle` — uncomment locally only if the upstream is unreachable, and never commit the change.

## Testing Guidelines

- JVM unit tests use JUnit 4 (declared in `gradle/libs.versions.toml` as `junit`). Tests live in `app/src/test/java/`. Add new tests next to the package they cover (e.g. `JsTest.kt` for scripting engine cases).
- Instrumented tests in `app/src/androidTest/java/` use the `androidTest` bundle (`androidx-junit`, `androidx-espresso-core`, `androidx-runner`). The runner is configured via `testInstrumentationRunner` in `app/build.gradle`.
- Name test classes `XxxTest` and methods with snake_case `operation_condition` (e.g. `addition_isCorrect`, `testMap`).
- Run `./gradlew :app:test` before opening a PR; add instrumented coverage for any change that touches Room migrations, networking, or the WebView bridge.

## Commit & Pull Request Guidelines

- Commit history mixes Chinese (`优化 #1234`) and Conventional Commits (`fix: ...`, `feat: ...`, `Bump web v3.26.x`). Either style is acceptable; reference the related issue number in the subject when one exists.
- The root `package.json` configures **Commitizen** with `cz-conventional-changelog` if you prefer interactive Conventional Commits.
- Pull requests should describe the change, link the issue it closes (`#NNNN`), and include screenshots or screen recordings for any UI change.
- Verify CI (`./github/workflows/test.yml`, `release.yml`, `web.yml`) is green and that `./gradlew assembleAppDebug` still succeeds on a clean checkout.
- Bug reports must follow `.github/ISSUE_TEMPLATE/01-bugReport.yml` — reproduce on the latest beta, exclude hook frameworks, and attach logs.

## Agent-Specific Notes

- When running shell commands inside this repo, prefer PowerShell-native cmdlets (`Get-ChildItem`, `Get-Content`) for read-only inspection; use the Gradle wrapper (`./gradlew`) rather than a system Gradle.
- Version bumps happen in `gradle/libs.versions.toml`; several entries carry `#noinspection` comments warning against upgrades — read those notes before changing a pinned version.
- Cronet jars in `app/cronetlib/` are vendored; updates flow through `.github/workflows/cronet.yml` and `app/download.gradle`.
- Release APK output is `legado_app_<versionName>.apk`; release builds require the `RELEASE_STORE_FILE`, `RELEASE_STORE_PASSWORD`, `RELEASE_KEY_ALIAS`, and `RELEASE_KEY_PASSWORD` Gradle properties.
