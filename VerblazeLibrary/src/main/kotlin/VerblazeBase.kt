import android.content.Context
import cache.DataStoreManager
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import managers.TranslationManager
import models.SupportedLanguagesData
import models.UserLanguage
import network.ApiFunctions
import network.ApiService

/*
The developer is allowed to use
->the functions below
->.vbt and .vbtWithLang()
->AutoTranslateWidget{content}

!!!!Before setting everything, the developer has to run configure because the SDK requires
context and apiKey to do other whole things.
 */
object VerblazeBase {
    internal lateinit var appContext: Context

    private val apiService = ApiService.apiService
    private val apiFunctions = ApiFunctions(apiService)

    object FunctionProvider {
        fun configure(context: Context, apiKey: String) {
            runBlocking {
                appContext = context
                apiFunctions.checkVersion(apiKey, DataStoreManager.getLastVersion())
            }
        }

        fun getSupportedLanguages(): String {
            return runBlocking {
                Json.encodeToString(
                    SupportedLanguagesData.serializer(),
                    DataStoreManager.getSupportedLanguages()
                )
            }
        }

        fun setCurrentLanguage(newLanguage: String) {
            runBlocking {
                TranslationManager.updateCurrentLanguageAndTranslations(newLanguage)
            }
        }

        fun getCurrentLanguage(): String {
            return runBlocking {
                Json.encodeToString(
                    serializer = UserLanguage.serializer(),
                    DataStoreManager.getCurrentLanguage()!!
                )
            }
        }
    }
}