package skin.data

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.nio.file.Path

object YmlData {
    fun saveSkin(path: Path, skinData: SkinData, isLegacy: Boolean = false) {
        val yml = path.toFile()
        YamlConfiguration.loadConfiguration(
            yml
        ).apply {
            if (isLegacy) {
                set("texture", null)
                set("signature", null)
            }
            set("skin", skinData.skin.toString())
            set("model", skinData.model.name)
            setComments("model", listOf(
                "CLASSIC, SLIM"
            ))
            save(yml)
        }
    }

    fun string(key: String, defValue: String, file: File): String {
        return YamlConfiguration.loadConfiguration(file).run {
            getString(key, defValue)!!
        }
    }
}