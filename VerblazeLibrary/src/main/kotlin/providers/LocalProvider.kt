package providers

import android.content.Context
import models.UserLanguage
import java.util.Locale

/*
If system language is within the supportedLanguages , currentLanguage will be it
Otherwise currentLanguage will be baseLanguage that developer already set on Verblaze
 */
//first element returns the system language
internal fun Context.currentLocale(): Locale = resources.configuration.locales[0]

//turning the locale into the object I'll use as UserLanguage
internal fun Locale.toLocaleInfo(): UserLanguage =
    UserLanguage(
        general = getDisplayName(Locale.ENGLISH),
        local = getDisplayName(this),
        code = toLanguageTag()
    )