package skin.handler

import org.bukkit.command.CommandExecutor
import skin.command.SkinCommand
import skin.system.Cardinal

object CommandHandler {
    private val commandMap = mapOf(
        "skin" to SkinCommand()
    )

    fun add(command: String, executor: CommandExecutor) {
        Cardinal.addCommand(command, executor)
    }

    fun all() {
        commandMap.forEach(::add)
    }
}