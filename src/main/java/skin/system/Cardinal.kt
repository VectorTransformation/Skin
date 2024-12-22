package skin.system

import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

object Cardinal {
    const val ADMINISTRATOR = "Skin"

    fun instance() = manager().getPlugin(ADMINISTRATOR) as JavaPlugin

    fun dataFolder() = instance().dataFolder

    fun manager() = Bukkit.getPluginManager()

    fun addCommand(command: String, executor: CommandExecutor) = instance().getCommand(command)?.setExecutor(executor)

    fun addListener(listener: Listener) = manager().registerEvents(listener, instance())

    fun log(msg: Any) {
        instance().logger.info(msg.toString())
    }
}