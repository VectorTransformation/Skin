package skin

import org.bukkit.plugin.java.JavaPlugin
import skin.data.FileData
import skin.system.command.CommandSystem
import skin.system.listener.ListenerSystem

class Skin : JavaPlugin() {
    override fun onEnable() {
        FileData.reloader()
        CommandSystem.executor()
        ListenerSystem.register()
    }
}