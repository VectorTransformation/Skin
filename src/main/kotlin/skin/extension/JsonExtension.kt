package skin.extension

import kotlinx.serialization.json.Json

private val format = Json {
    isLenient = true
    prettyPrint = true
    ignoreUnknownKeys = true
}

fun Json.format(): Json {
    return format
}