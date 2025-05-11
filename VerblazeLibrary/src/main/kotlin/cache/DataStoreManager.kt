package cache

/*
It is quit simple. I referred the keys to each informations I need
and there are functions for them as save and get functions
 */
import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import models.SupportedLanguagesData
import kotlinx.serialization.json.Json
import models.MultipleTranslations
import models.TranslationFile
import models.UserLanguage

private val Context.dataStore by preferencesDataStore(name = "verblaze")

internal object DataStoreManager {
    private val KEY_SUPPORTEDLANGUAGES = stringPreferencesKey("supported_languages")
    private val KEY_WHOLETRANSLATIONS = stringPreferencesKey("whole_translations")
    private val KEY_CURRENT_TRANSLATIONS = stringPreferencesKey("translations_for_current_language")
    private val KEY_CURRENT_LANGUAGE = stringPreferencesKey("current_language")
    private val KEY_VERSION = intPreferencesKey("translation_version")

    /////////////////////
    //SUPPORTED_LANGUAGES
    suspend fun saveSupportedLanguages(datas: SupportedLanguagesData) {
        val serializier = SupportedLanguagesData.serializer()
        val jsonString = Json.encodeToString(serializier, datas)
        VerblazeBase.appContext.dataStore.edit { it[KEY_SUPPORTEDLANGUAGES] = jsonString }
    }

    suspend fun getSupportedLanguages(): SupportedLanguagesData {
        val serializer = SupportedLanguagesData.serializer()
        val dataString =
            VerblazeBase.appContext.dataStore.data.map { it[KEY_SUPPORTEDLANGUAGES] }.first()

        return dataString?.let { Json.decodeFromString(serializer, it) } ?: run {
            Log.e(
                "getSupportedLanguages",
                "NullPointerException : supportedLanguages is null\nDataStoreManager.getSupportedLanguages()"
            )
            SupportedLanguagesData(UserLanguage("", "", ""), emptyList())
        }
    }

    ////////////////////
    //WHOLE_TRANSLATIONS
    suspend fun saveWholeTranslations(datas: MultipleTranslations) {
        val serializer = MultipleTranslations.serializer()
        val jsonString = Json.encodeToString(serializer, datas)
        VerblazeBase.appContext.dataStore.edit { it[KEY_WHOLETRANSLATIONS] = jsonString }
    }

    suspend fun getWholeTranslations(): MultipleTranslations {
        val serilalizer = MultipleTranslations.serializer()
        val dataString =
            VerblazeBase.appContext.dataStore.data.map { it[KEY_WHOLETRANSLATIONS] }.first()
        return dataString?.let { Json.decodeFromString(serilalizer, it) } ?: run {
            Log.e(
                "getWholeTranslations",
                "NullPointerException : wholeTranslations is null\nDataStoreManager.getWholeTranslations()"
            )
            MultipleTranslations(emptyMap(), emptyList())
        }
    }

    //////////////////////
    //CURRENT_LANGUAGE
    suspend fun saveCurrentLanguage(newlanguage: UserLanguage) {
        val serializer = UserLanguage.serializer()
        val jsonString = Json.encodeToString(serializer, newlanguage)
        VerblazeBase.appContext.dataStore.edit { it[KEY_CURRENT_LANGUAGE] = jsonString }
    }

    suspend fun getCurrentLanguage(): UserLanguage? {
        val serializer = UserLanguage.serializer()
        val dataString =
            VerblazeBase.appContext.dataStore.data.map { it[KEY_CURRENT_LANGUAGE] }.first()
        return dataString?.let { Json.decodeFromString(serializer, it) } ?: run {
            Log.e(
                "getCurrentLanguage",
                "NullPointerException : currentLanguage is null\nDataStoreManager.getCurrentLanguage()"
            )
            null
        }
    }

    //////////////////////
    //CURRENT_TRANSLATIONS
    suspend fun saveCurrentTranslations(translationfile: List<TranslationFile>) {
        val serializer = ListSerializer(TranslationFile.serializer())
        val jsonString = Json.encodeToString(serializer, translationfile)
        VerblazeBase.appContext.dataStore.edit { it[KEY_CURRENT_TRANSLATIONS] = jsonString }
    }

    suspend fun getCurrentTranslations(): List<TranslationFile> {
        val serializer = ListSerializer(TranslationFile.serializer())
        val dataString =
            VerblazeBase.appContext.dataStore.data.map { it[KEY_CURRENT_TRANSLATIONS] }.first()
        return dataString?.let {
            Json.decodeFromString(serializer, it)
        } ?: run {
            Log.e(
                "getCurrentTranslations",
                "NullPointerException : currentTranslations is null\nDataStoreManager.getCurrentTranslations()"
            )
            emptyList()
        }
    }

    //////////////////
    //VERSION_CHECKİNG
    suspend fun saveLastVersion(lastVersion: Int) {
        VerblazeBase.appContext.dataStore.edit { it[KEY_VERSION] = lastVersion }
    }

    suspend fun getLastVersion(): Int {
        return VerblazeBase.appContext.dataStore.data.first()[KEY_VERSION] ?: 1
    }

}




