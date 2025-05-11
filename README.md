# Verblaze Kotlin

Verblaze Kotlin is a simple and efficient multilingual (i18n) management for Kotlin Developers. It lets the developers manage the languages they use in the application by controlling whole things in Verblaze.com. Verblaze has AI system to translate the texts much faster and to add the languages in your app.

## Features

- 🚀 Simple and easy-to-use API
- 💾 Built-in cache support
- ⚡ High-performance translation processing
- 🔄 Dynamic language switching
- 📱 Optimized for Kotlin applications
- 🔍 Type-safe translation keys
- 🌐 Automatic language detection
- 📦 Minimal setup required

## Installation

The implementation you need are below.

```kotlin
//In build.gradle.kts(:app)
dependencies{
    implementation("com.github.Verblaze:verblaze_jetpack_compose.v1.0.0") //Dont forget to use latest version
}

//In settings.gradle.kts
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") } //Add here! Here is important to compile the module
    }
}
```

#### !!Your compose-compiler version has to be 2.0+. Set the compose-compiler to use Jetpack Compose properly.!!

## Setup

### 1. Initialize Verblaze

    Firstly initialize your Verblaze Function Provider and run the function "configure".
    configure have to be run before the others. It sets the context that Verblaze Functions need.

```kotlin
    val VerblazeProvider = VerblazeBase.FunctionProvider
    VerblazeProvider.configure(context,"YOUR_API_TOKEN")
```

## Usage

### 1.AutoTranslateWidget

-->Creates widgets that automatically recompose the screen when current language is changed
->If you dont use `.vbt` or `.vbtWithLang()` in the composable function, you dont have to use AutoTranslatedWidget{},
because you dont have to recompose the screen when current language is changed

```kotlin
    @Composable
    fun TestPage(){
        AutoTranslatedWidget{
            Scaffold(modifier=Modifier){paddingValue->
            Column(modifier=Modifier.padding(paddingValue)){
                Text(text="Your content must be wrapped by AutoTranslatedWidget{} as I did here")
                Text(text="IntroductionPage.Hello".vbt)
                }
            }
        }
    }
```

### 2.Basic Translation

--->Use `.vbt` extension on your text that you want to translate
-->Text must be in the correct form as "file_key.value_key".vbt
->It translates the string to the current language and returns it if "file_key.value_key" is matched. Otherwise, it returns "file_key.value_key" as the same.

```kotlin
Text(text="IntroductionPage.Hello".vbt)
```

### 3.Translate to The Specific Language

--->Use `.vbtWithLang(languageCode)` function on your text.
-->If the language code matches with one of supported languages that you set on Verblaze.com, returns its translated form.
Otherwise, it returns the String as the same.

```kotlin
Text(text="IntroductionPage.Hello".vbtWithLang(es-ES))
```

### 4. Translation Key Format

Translation keys must follow the format: `file_key.translation_key`

Examples:

```kotlin
'home_view.welcome_message'.vbt
'settings.language_selection'.vbt
'auth.login_button'.vbt
```

### 5.Language Management

--->You can get supported languages , set current language, get current language.

```kotlin
//Get Supported Languages
val supportedLanguagesAsString = VerblazeProvider.getSupportedLanguages() //returns as Json
For example :
{
  "baseLanguage": {
    "code": "en-US",
    "general": "English(United States)",
    "local" : "English(United States)"
  },
  "supportedLanguages": [
    {
    "code": "en-US",
    "general": "English(United States)",
    "local" : "English(United States)"
    },
    {
    "code": "tr-TR",
    "general": "Turkish(Turkiye)",
    "local" : "Türkçe(Türkiye)"
    }
  ]
}


//Set Current Language
VerblazeProvider.setCurrentLanguage(languageCode)
languageCode is like "en-US" , "es-ES" , "tr-TR"
For example:
            VerblazeProvider.setCurrentLanguage("es-ES")

//Get Current Language
val currentLanguageAsstring = VerblazePRovider.getCurrentLanguage()
For example :
    {
    "code": "en-US",
    "general": "English(United States)",
    "local" : "English(United States)"
    }
```

## Advanced Features

### 1. Caching

Verblaze automatically caches translations and manages version control. The cache is automatically invalidated when:

- A new version of translations is available
- The language is changed
- The app is reinstalled

### 2. Language Detection

Verblaze automatically detects and restores the last used language when the app starts.

### 3. Version Control

Verblaze maintains version control of translations and automatically updates them when new versions are available from the server.

## Example

```kotlin
//Data Classes for language object and supportedLanguages object
@Serializable
data class UserLanguage(){
    val code: String,
    val general: String,
    val local : String
}
@Serializable
internal data class SupportedLanguagesData(
    val baseLanguage: UserLanguage,
    val supportedLanguages: List<UserLanguage>
)

val jsonObject : UserLanguage = Json.decodeFromString(UserLanguage.serializer(),VerblazeProvider.getCurrentLanguage())
val selectedLanguage : MutableState<UserLanguage> = mutableStateOf(jsonObject)

val languageList : List<UserLanguage> = Json.decodeFromString(VerblazeProvider.getSupportedLanguages()).supportedLanguages

@Composable
fun TestPage(){
    var isExpanded by remember { mutableStateOf(false) }
    AutoTranslatedWidget{
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = selectedLanguage.local,
                    modifier = Modifier.clickable(onClick = { isExpanded = !isExpanded })
                )
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                ) {
                    languageList.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item.local) },
                            onClick = {
                                selectedLanguage.value = item
                                verblazeProvider.setCurrentLanguage(item.code)
                                isExpanded = false
                            }
                        )
                    }
                }
                Button(
                    onClick = {
                        navController.navigate(route = "AnotherPage")
                    }
                ) {
                    Text(text = "testpage.helloWorld".vbt)
                }
            }
        }
    }
}
```

## Best Practices

1. Always use the `AutoTranslatedWidget` for screens that contain translations
2. Follow the `file_key.translation_key` format for all translation keys
3. Use meaningful file keys that match your screen/component names
4. Keep translation keys lowercase and use underscores for spaces

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more information.

## Release Notes

See [CHANGELOG.md](CHANGELOG.md) for changes.
