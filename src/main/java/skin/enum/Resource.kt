package skin.enum

import skin.extension.*
import skin.system.Cardinal
import java.io.File

private val dataFolder = Cardinal.dataFolder()

enum class Resource(val file: File) {
    ABYSS(dataFolder.child("abyss")),
    CACHE(dataFolder.child("cache")),
    SKIN(dataFolder.child("skin"));

    fun child(child: String) = file.child(child)

    fun make() = file.make()

    fun save(content: String) = file.save(content)

    fun save(content: ByteArray) = file.save(content)

    fun forEach(
        filter: String? = null,
        action: (File) -> Unit
    ) = file.forEach(filter, action)

    fun clear() = file.clear()
}