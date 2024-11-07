package skin

import org.bukkit.plugin.java.JavaPlugin
import skin.data.FileData
import skin.system.CommandSystem
import skin.system.ListenerSystem

class Skin : JavaPlugin() {
    override fun onEnable() {
        FileData.reloader()
        CommandSystem.executor()
        ListenerSystem.register()
    }
}