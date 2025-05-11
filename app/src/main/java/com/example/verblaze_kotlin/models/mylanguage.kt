package com.example.verblaze_kotlin.models

import kotlinx.serialization.Serializable

@Serializable
internal data class mylanguage(
    val code: String,
    val general: String,
    val local : String
)