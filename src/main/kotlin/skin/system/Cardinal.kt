package skin.system

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

object Cardinal {
    const val ADMINISTRATOR = "Skin"

    fun instance() = manager().getPlugin(ADMINISTRATOR) as JavaPlugin

    fun dataFolder() = instance().dataFolder

    fun manager() = Bukkit.getPluginManager()

    fun lifecycleManager() = instance().lifecycleManager

    fun addLifecycleCommand(
        command: LiteralArgumentBuilder<CommandSourceStack>,
        description: String? = null,
        aliases: Collection<String> = listOf()
    ) = lifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS) { commands ->
        commands.registrar().register(command.build(), description, aliases)
    }

    fun addCommand(command: String, executor: CommandExecutor) = instance().getCommand(command)?.setExecutor(executor)

    fun addListener(listener: Listener) = manager().registerEvents(listener, instance())

    fun removeListener(listener: Listener) = HandlerList.unregisterAll(listener)

    fun log(msg: Any) {
        instance().logger.info(msg.toString())
    }
}