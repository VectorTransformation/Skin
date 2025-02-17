package skin.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands

abstract class CustomCommand {
    abstract val command: LiteralArgumentBuilder<CommandSourceStack>
    open val description: String? = null
    open val aliases: Collection<String> = listOf()

    fun builder(command: String) = Commands.literal(command)
}