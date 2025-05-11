package network

import models.CheckVersionRequest
import models.CheckVersionResponse
import models.MultipleTranslationRequest
import models.MultipleTranslationResponse
import models.SupportedLanguagesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
/*
Clearly API request-functions
 */
internal interface ApiClient {
    @GET("v1/supported-languages")
    suspend fun fetchSupportedLanguages(@Header("x-api-key") apiKey: String): Response<SupportedLanguagesResponse>

    @POST("v1/version-check")
    suspend fun checkVersion(
        @Header("x-api-key") apiKey: String,
        @Header("Content-Type") contentType : String = "application/json",
        @Body request : CheckVersionRequest
    ):Response<CheckVersionResponse>


    @POST("v1/translations/multiple")
    suspend fun fetchMultipleTranslations(
        @Header("x-api-key") apiKey:String,
        @Header("Content-Type") contentType : String = "application/json",
        @Body request : MultipleTranslationRequest
    ):Response<MultipleTranslationResponse>
}