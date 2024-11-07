package skin.system

import org.bukkit.command.CommandExecutor
import skin.command.SkinCommand

object CommandSystem {
    fun executor(command: String, executor: CommandExecutor) {
        Cardinal.instance().getCommand(command)?.setExecutor(executor)
    }

    fun executor() {
        executor("skin", SkinCommand())
    }
}