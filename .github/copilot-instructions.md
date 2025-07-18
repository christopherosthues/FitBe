---
description: 'Guidelines for building Kotlin Compose Multiplatform applications'
applyTo: '**/*.kt'
---
# GitHub Copilot Instructions

This project uses **Kotlin**, **Compose Multiplatform** from JetBrains and Gradle, along with several common libraries.

---

## General Instructions
- Make only high confidence suggestions when reviewing code changes.
- Write code with good maintainability practices, including comments on why certain design decisions were made.
- Handle edge cases and write clear exception handling.
- For libraries or external dependencies, mention their usage and purpose in comments.
- Ensure all generated code follows the project's style and conventions.
- Always review, test, and refactor Copilot suggestions as needed.

## Kotlin-Specific Instructions

- Prefer idiomatic Kotlin: use data classes, extension functions, and null safety features.
- Use coroutines for asynchronous operations.
- Leverage Kotlin's standard library functions (e.g., `let`, `apply`, `run`, `with`, `also`).
- Avoid Java-style code and unnecessary verbosity.
- Prefer EnumClassName.entries over EnumClassName.values()

## Compose Multiplatform (JetBrains)

- Use `@Composable` functions for UI components.
- Structure UI code using state hoisting and unidirectional data flow.
- Prefer `remember` and `mutableStateOf` for state management.
- Use themes and Material components where possible.
- Organize UI code into reusable composable functions.

## Common Libraries

- Use **Koin** for dependency injection. Apply DI where possible and useful.
- All database operations must use **Android Room** and **androidx.sqlite**. As data type for the id
  Uuid from kotlin.uuid.Uuid should be used. Never autogenerate the id from the database. Use
  Uuid.random() to initialize it.
- For plotting, use **Koalaplot** ([koalaplot-core](https://github.com/KoalaPlot/koalaplot-core)):
  - All plots must be wrapped in a `ChartLayout`.
  - Many plot types must also be wrapped in an `XYGraph`.
  - Available Plot, Chart and Graph classes from Koalaplot include:
    - `BulletGraph`
    - `GroupedVerticalBarPlot`
    - `StackedVerticalBarPlot`
    - `VerticalBarPlot`
    - `LinePlot`
    - `PieChart`
    - `PolarGraph`
    - `PolarPLotSeries`
    - `XYGraph`
- Do **not** use any JVM-specific classes.
- For all date and time types, use the library **kotlinx.datetime** (version 0.6.0).
- Mark experimental API usages with @OptIn(<Classname>::class). This includes class from
  kotlinx.datetime (ExperimentalTime::class), some Material3 classes (ExperimentalMaterial3Api::
  class)) and Uuid (ExperimentalUuidApi::class) or the kotlin.uuid package.

## Architecture

- All code resides in the packages under org.darthacheron.fitbe.
- All shared components are located in the components package.
- All shared classes that are injected via DI are registered in the Modules.kt inside the di package.
- Use constructor injection only.
- All platform dependent classes that are injected via DI are registered in the platform specific
  modules also in the Modules.<platform>.kt files.
- All utility classes and functions reside in the utils package.
- The application uses the MVVM and Repository patterns. The repositories uses the specific Room DAOs.
- For each feature there should be a separate DAO interface.
- The FitBeDatabase has a property for each DAO.
- Each DAO is registered as a singleton in the Koin module class (Modules.kt in the di package of
  the composeApp module)
- All entity classes should have the suffix Entity.
- Use in the model classes the UInt class, for entity classes the Int class.
- All ViewModel classes should inherit from androidx.lifecycle.ViewModel
- All Screen classes should follow the naming pattern <FeatureName>View.
- All libraries and plugins are maintained in a version catalogue.

## Resources

- Localize all displayed text. Supported languages are English (values/strings.xml) and German (
  values-de/strings.xml). Both languages are mandatory. The resource files are in the
  composeResources folder of the composeApp module (composeApp/src/commonMain/composeResources). Use
  the Res.string and Res.plurals properties generated in the Res object. The localized texts are
  stored in Android XML format.
- The Res.string properties are of type StringResource. In the UI the texts used properties have to
  be wrapped in a stringResource() function call. Same for the Res.plurals properties.
- Generate Android drawable icons from the Android library of Android Studio (filled category not
  Material!). Use Hexcode colors instead of Android colors in the drawables.
- Do **not** use the icons of the Icons class from Android because they are not cross-platform.

## Documentation & Testing

- Add or update unit tests for new features or bug fixes.
- Ensure code is cross-platform where applicable. In case there is code that is not cross-platform
  make sure that it resides in the correct module for the corresponding platform. Make sure that the
  non-cross-platform feature is implemented for all platforms then in the platform specific modules.

## Security & Privacy

- Never include secrets, credentials, or sensitive data in code or comments.
- Scan code for security issues and vulnerabilities and mention them.

---

_Last updated: 2024-06_

