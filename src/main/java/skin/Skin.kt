package skin

import org.bukkit.plugin.java.JavaPlugin
import skin.handler.CommandHandler
import skin.handler.ListenerHandler
import skin.handler.SkinHandler

class Skin : JavaPlugin() {
    override fun onEnable() {
        SkinHandler.reload()
        ListenerHandler.all()
        CommandHandler.all()
    }
}