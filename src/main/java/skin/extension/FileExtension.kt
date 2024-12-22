package skin.extension

import java.io.File
import java.io.FileFilter

fun File.child(child: String) = File(this, child)

fun File.make() = apply {
    parentFile?.mkdirs()
    if (extension.isEmpty()) mkdir() else createNewFile()
}

fun File.save(content: String) = save(content.toByteArray())

fun File.save(content: ByteArray) = apply {
    writeBytes(content)
}

fun File.forEach(filter: String? = null, action: (File) -> Unit) = apply {
    val fileFilter = filter?.run {
        object : FileFilter {
            override fun accept(file: File) = file.extension == filter
        }
    }
    listFiles(fileFilter)?.forEach(action)
}

fun File.clear() = apply {
    deleteRecursively()
}