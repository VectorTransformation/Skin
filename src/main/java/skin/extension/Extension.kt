package skin.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import skin.data.FileData
import java.nio.file.Path
import java.nio.file.Paths
import java.text.DecimalFormat
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.pathString
import kotlin.system.measureTimeMillis

private val format = Json {
    isLenient = true
    prettyPrint = true
    ignoreUnknownKeys = true
}

fun Json.format(): Json {
    return format
}

fun Path.isFile(): Boolean {
    return !name.startsWith(".") && "." in name
}

fun Path.skinName(): String {
    return nameWithoutExtension
}

fun Path.skinParentName(): String {
    return pathString.substring(0, pathString.length - name.length).substring(
        Paths.get(
            FileData.dataFolderPath,
            FileData.skin
        ).pathString.length + 1
    ).replace("\\", "/")
}

private val decimalFormat = DecimalFormat("#,###")

fun Long.decimalFormat(): String {
    return decimalFormat.format(this)
}

fun Int.decimalFormat(): String {
    return decimalFormat.format(this)
}

@OptIn(ExperimentalEncodingApi::class)
fun String.base64ToString(): String {
    return Base64.decode(toByteArray()).toString(Charsets.UTF_8)
}

val scope = CoroutineScope(Dispatchers.Default)

val ioScope = CoroutineScope(Dispatchers.IO)

fun test(function: () -> Unit) {
    measureTimeMillis {
        function.invoke()
    }.apply {
        println("${decimalFormat()} ms")
    }
}