package skin.system.command

import org.bukkit.command.CommandExecutor
import skin.command.SkinCommand
import skin.system.Cardinal

object CommandSystem {
    val executorMap = mapOf(
        "skin" to SkinCommand()
    )

    fun executor() {
        executorMap.forEach(::executor)
    }

    fun executor(command: String, executor: CommandExecutor) {
        Cardinal.instance().getCommand(command)?.setExecutor(executor)
    }
}