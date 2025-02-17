package skin.extension

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun String.base64ToString(): String {
    return Base64.decode(toByteArray()).toString(Charsets.UTF_8)
}