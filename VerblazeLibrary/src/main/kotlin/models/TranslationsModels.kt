package models

import kotlinx.serialization.Serializable
/*
Data Types for translation values and their requests and responses
 */
@Serializable
internal data class MultipleTranslationRequest(val languages: List<String>)

@Serializable
internal data class MultipleTranslationResponse(
    val `data`: MultipleTranslations,
    val message: String,
    val statusCode: Int
)

@Serializable
internal data class MultipleTranslations(
    val translations: Map<String, List<TranslationFile>>,
    val requestedLanguages : List<String>
)

@Serializable
internal data class TranslationFile(
    val file_title: String,
    val file_key: String,
    val values: Map<String, String>
)






