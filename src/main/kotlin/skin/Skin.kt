package skin

import org.bukkit.plugin.java.JavaPlugin
import skin.handler.LifecycleHandler
import skin.handler.ListenerHandler
import skin.manager.SkinManager

class Skin : JavaPlugin() {
    override fun onEnable() {
        SkinManager.reload()
        ListenerHandler.all()
        LifecycleHandler.all()
    }
}