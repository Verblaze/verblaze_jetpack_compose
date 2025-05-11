package com.example.verblaze_kotlin.cache

import androidx.lifecycle.ViewModel
import com.example.verblaze_kotlin.models.SupportedLanguagesData
import com.example.verblaze_kotlin.models.mylanguage
import kotlinx.serialization.json.Json

internal class exampleViewModel(
    VerblazeProvider : VerblazeBase.FunctionProvider
): ViewModel() {
    private val serializer = mylanguage.serializer()
    var selectedLanguage : mylanguage = Json.decodeFromString(serializer,VerblazeProvider.getCurrentLanguage())
    val optionlist: List<mylanguage> =
        Json.decodeFromString<SupportedLanguagesData>(
            VerblazeProvider.getSupportedLanguages()
        ).supportedLanguages
}