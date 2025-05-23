package network

import android.util.Log
import cache.DataStoreManager
import models.CheckVersionRequest
import models.MultipleTranslationRequest
import models.TranslationFile
import models.UserLanguage
import providers.currentLocale
import providers.toLocaleInfo
import retrofit2.HttpException
/*
Here I call the API requests and use the responses how i need to use
 */
internal class ApiFunctions(
    private val apiClient: ApiClient
) {

    suspend fun checkVersion(apiKey: String, lastVersion: Int) {
        try {
            //request about checking version
            val response = apiClient.checkVersion(
                apiKey,
                request = CheckVersionRequest(lastVersion)
            )
            if (response.isSuccessful) {
                //If response is successfull, it writes the log as statusCode and message
                Log.e(
                    "checkVersion",
                    "StatusCode : ${response.body()?.statusCode}\nMessage : ${response.body()?.message}"
                )
                if (response.body()?.data?.needsUpdate == true) {
                    /*
                    If update is needed, saves the latest version and
                    fetch whole supportedLanguages and translations again
                     */
                    DataStoreManager.saveLastVersion(response.body()?.data?.latestVersion?:1)
                    setSupportedLanguages(apiKey)
                }
            } else {
                Log.e(
                    "Err_CheckVersion",
                    "ApiFunctions().checkVersion()\n${HttpException(response).localizedMessage}"
                )
            }
        } catch (e: Exception) {
            Log.e("checkVersion_api", "ApiFunctions().checkVersion()\n${e.localizedMessage}")
        }
    }

    suspend fun setSupportedLanguages(apiKey: String) {
        try {
            val response = apiClient.fetchSupportedLanguages(apiKey)
            if (response.isSuccessful) {
                Log.e(
                    "setSupportedLanguages",
                    "StatusCode : ${response.body()?.statusCode}\nMessage : ${response.body()?.message}"
                )
                DataStoreManager.saveSupportedLanguages(response.body()?.data!!)
                if (DataStoreManager.getCurrentLanguage()?.code=="") {
                    /*
                    if currentLanguage is null, it means user logs in for first,
                    so I need to set currentLanguage that can be change in the future again
                     */
                    val systemLanguage: UserLanguage =
                        VerblazeBase.appContext.currentLocale().toLocaleInfo()
                    val currentLanguage: UserLanguage =
                        response.body()?.data?.supportedLanguages?.find { it.code == systemLanguage.code }
                            ?: response.body()?.data?.baseLanguage!!
                    DataStoreManager.saveCurrentLanguage(currentLanguage)
                }
                setMultipleTranslations(
                    apiKey,
                    response.body()?.data?.supportedLanguages?.map { it.code }!!,
                    DataStoreManager.getCurrentLanguage()?: UserLanguage("","","")
                )
            } else {
                Log.e(
                    "Err_supportedLanguages",
                    "ApiFunctions().setSupportedLanguages()\n${HttpException(response).localizedMessage}"
                )
            }
        } catch (e: Exception) {
            Log.e(
                "setSupportedLanguages",
                "ApiFunctions().setSupportedLanguages()\n${e.localizedMessage}"
            )
        }
    }

    suspend fun setMultipleTranslations(
        apiKey: String,
        codeList: List<String>,
        currentLanguage: UserLanguage
    ) {
        /*
        I got currentLanguage as parameter because when I fetched whole translations I am gonna
        set the currentTranslations by taking it from wholeTranslations
         */
        try {
            val response = apiClient.fetchMultipleTranslations(
                apiKey,
                request = MultipleTranslationRequest(codeList)
            )
            if (response.isSuccessful) {
                Log.e(
                    "setMultipleTranslations",
                    "StatusCode : ${response.body()?.statusCode}\nMessage : ${response.body()?.message}"
                )
                DataStoreManager.saveWholeTranslations(response.body()?.data!!)
                val currentTranslations: List<TranslationFile> =
                    response.body()?.data?.translations?.get(currentLanguage.code)?:emptyList()
                DataStoreManager.saveCurrentTranslations(currentTranslations)
            } else {
                Log.e(
                    "Err_multipleTranslations",
                    "ApiFunctions().setMultipleTranslations()\n${HttpException(response).localizedMessage}"
                )
            }
        } catch (e: Exception) {
            Log.e(
                "setMultipleTranslations",
                "ApiFunctions().setMultipleTranslations()\n${e.localizedMessage}"
            )
        }
    }
}