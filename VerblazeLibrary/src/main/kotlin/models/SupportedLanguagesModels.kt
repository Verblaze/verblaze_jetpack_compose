package models

import kotlinx.serialization.Serializable
/*
Data Types for supportedLanguge requests and responses
 */
@Serializable
internal data class SupportedLanguagesResponse(
    val data: SupportedLanguagesData,
    val message: String,
    val statusCode: Int
)

@Serializable
internal data class SupportedLanguagesData(
    val baseLanguage: UserLanguage,
    val supportedLanguages: List<UserLanguage>
)

/*
This data class contains the informations about any language
->For example
    code : "tr-TR"
    general : Turkish(Turkiye)
    local : Turkce(Turkiye)
 */
@Serializable
internal data class UserLanguage(
    val code: String,
    val general: String,
    val local : String
)
