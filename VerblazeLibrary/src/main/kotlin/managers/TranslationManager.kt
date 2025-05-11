package managers

import android.util.Log
import cache.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import models.MultipleTranslations
import models.TranslationFile
import models.UserLanguage
import java.lang.Exception

/*
->It fetchs the datas needed from context and keep here to reach them easier in need.
->Function "translate" translates the text to "the current language" and returns its translated version
->Function translateWithLang() translates the text to "the language that is wanted by developer"
    and returns its translated version

->updateCurrentLanguageAndTranslations : When currentLanguage is change,
    the informations as currentLanguage,currentTranslations are also change in context, thanks to this function
 */
internal object TranslationManager {
    internal val currentLanguage: MutableStateFlow<UserLanguage> = runBlocking {
        MutableStateFlow(DataStoreManager.getCurrentLanguage()!!)
    }
    private val currentTranslations: MutableStateFlow<List<TranslationFile>> = runBlocking {
        MutableStateFlow<List<TranslationFile>>(DataStoreManager.getCurrentTranslations())
    }
    private val wholeTranslations: MutableStateFlow<MultipleTranslations?> =
        runBlocking { MutableStateFlow<MultipleTranslations?>(DataStoreManager.getWholeTranslations()) }

    suspend fun updateCurrentLanguageAndTranslations(newLanguage: String) {
        try {
            val currentLanguageWillBeSet: UserLanguage =
                DataStoreManager.getSupportedLanguages().supportedLanguages.find { it.code == newLanguage }!!
            DataStoreManager.saveCurrentLanguage(currentLanguageWillBeSet)
            DataStoreManager.saveCurrentTranslations(
                wholeTranslations.value?.translations?.get(
                    newLanguage
                )!!
            )

            currentLanguage.value = currentLanguageWillBeSet
            currentTranslations.value =
                wholeTranslations.value?.translations?.get(newLanguage)!!
        } catch (e: Exception) {
            Log.e(
                "updateCurrentLanguageAndTranslations",
                "TranslationManager.updateCurrentLanguageAndTranslations()\n${e.localizedMessage}"
            )
        }
    }

    fun translate(text: String): String {
        if (text.isEmpty()) {
            Log.e(
                "TranslationManager.translate()",
                "Text $text,that you entered to translate, is empty"
            )
        }
        if (currentTranslations.value.isEmpty()) {
            Log.e(
                "TranslationManager.translate()",
                "Translations for ${currentLanguage.value.code} was not found"
            )
        }
        val parts = text.split(".", limit = 2)
        if (parts[1].isEmpty() || parts[1].isEmpty() || parts.size != 2) {
            Log.e(
                "TranslationManager.translate()",
                "Invalid translation key format. Use: file_key.translation_key"
            )
        }
        val fileKey = parts[0]
        val valueKey = parts[1]
        val values: Map<String, String>? =
            currentTranslations.value.firstOrNull { it.file_key == fileKey }?.values
        return values?.get(valueKey) ?: text
    }

    fun translateWithLang(text: String, languageCode: String): String {
        if (text.isEmpty()) {
            Log.e(
                "TranslationManager.translate()",
                "Text $text,that you entered to translate, is empty"
            )
        }
        val selectedTranslations: List<TranslationFile>? =
            wholeTranslations.value?.translations?.get(languageCode)
        if (selectedTranslations.isNullOrEmpty()) {
            Log.e("TranslationManager.translate()", "Tranlastions for $languageCode was not found")
        }
        val parts = text.split(".", limit = 2)
        if (parts[1].isEmpty() || parts[1].isEmpty() || parts.size != 2) {
            Log.e(
                "TranslationManager.translate()",
                "Invalid translation key format. Use: file_key.translation_key"
            )
        }
        val fileKey = parts[0]
        val valueKey = parts[1]
        val values: Map<String, String>? =
            selectedTranslations?.firstOrNull { it.file_key == fileKey }?.values
        return values?.get(valueKey) ?: text
    }
}