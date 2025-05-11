package extensions

import managers.TranslationManager
/*
String.vbt translates the String to current language
String.vbtWithLang(languagecode) translates it to the specific language
 */
val String.vbt: String
    get() {
        return TranslationManager.translate(this)
    }

fun String.vbtWithLang(languagecode: String): String {
    return TranslationManager.translateWithLang(this, languagecode)
}