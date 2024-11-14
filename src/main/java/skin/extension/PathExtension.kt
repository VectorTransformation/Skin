package skin.extension

import skin.data.FileData
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.pathString

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

fun Path.skinId(): String {
    return skinParentName().plus(skinName())
}