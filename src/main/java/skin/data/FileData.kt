package skin.data

import org.bukkit.entity.Entity
import skin.extension.isFile
import skin.system.Cardinal
import skin.system.SkinSystem
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.createParentDirectories
import kotlin.io.path.exists

object FileData {
    val dataFolderPath = Cardinal.instance().dataFolder.path
    val cache = "cache"
    val skin = "skin"
    val abyss = "abyss"

    fun reloader(sender: Entity? = null) {
        dataFolder()
        SkinSystem.reloader(sender)
    }

    fun dataFolder() {
        listOf(
            cache,
            skin,
            abyss
        ).forEach {
            Paths.get(
                dataFolderPath,
                it
            ).run {
                creater(this)
            }
        }
    }

    fun creater(path: Path): Boolean {
        with(path) {
            if (exists()) {
                return false
            }
            if (isFile()) {
                createParentDirectories()
                createFile()
            } else {
                createDirectories()
            }
            return true
        }
    }
}