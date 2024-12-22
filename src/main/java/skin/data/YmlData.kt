package skin.data

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object YmlData {
    fun saveSkin(yml: File, skinData: SkinData, isLegacy: Boolean = false) {
        YamlConfiguration.loadConfiguration(yml).apply {
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