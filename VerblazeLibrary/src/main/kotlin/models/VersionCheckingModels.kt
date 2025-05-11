package models

/*
Data Types for check-version requests and resopnses
 */
internal data class CheckVersionRequest(
    val currentVersion:Int
)

internal data class CheckVersionResponse(
    val `data`: CheckVersionData,
    val message: String,
    val statusCode: Int
)
internal data class CheckVersionData(
    val latestVersion: Int,
    val needsUpdate: Boolean
)

