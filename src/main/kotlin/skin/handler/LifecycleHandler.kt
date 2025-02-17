package skin.handler

import skin.command.CustomCommand
import skin.command.SkinCommand
import skin.system.Cardinal

object LifecycleHandler {
    private val commandSet = setOf(
        SkinCommand()
    )

    fun add(command: CustomCommand) {
        Cardinal.addLifecycleCommand(command.command, command.description, command.aliases)
    }

    fun all() {
        commandSet.forEach(::add)
    }
}