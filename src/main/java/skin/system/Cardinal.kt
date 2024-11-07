package skin.system

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin

object Cardinal {
    const val ADMINISTRATOR = "Skin"

    fun instance(): JavaPlugin {
        return Bukkit.getPluginManager().getPlugin(
            ADMINISTRATOR
        ) as JavaPlugin
    }

    fun logger(): ComponentLogger {
        return instance().componentLogger
    }

    fun info(component: Component) {
        return logger().info(component)
    }

    fun pluginManager(): PluginManager {
        return Bukkit.getPluginManager()
    }

    fun registerEvent(listener: Listener) {
        pluginManager().registerEvents(listener, instance())
    }
}