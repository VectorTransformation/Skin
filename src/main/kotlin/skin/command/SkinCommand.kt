package skin.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import skin.manager.SkinManager

class SkinCommand : CustomCommand() {
    override val command = builder("skin").apply {
        requires(::isOp)
        then(Commands.literal("reload").executes(::reload))
        then(Commands.literal("reset")
            .then(Commands.argument("player", ArgumentTypes.players())
                .executes(::reset)
            ))
        then(Commands.literal("save")
            .then(Commands.argument("player", ArgumentTypes.player())
                .then(Commands.argument("skin", StringArgumentType.word())
                    .suggests { _, builder -> builder.suggest("tmp").buildFuture() }
                    .executes(::save)
                )))
        then(Commands.literal("set")
            .then(Commands.argument("player", ArgumentTypes.players())
                .then(Commands.argument("skin", StringArgumentType.word())
                    .suggests { _, builder ->
                        SkinManager.skinList().filter {
                            it.lowercase().startsWith(builder.remainingLowerCase)
                        }.forEach {
                            builder.suggest(it)
                        }
                        builder.buildFuture()
                    }
                    .executes(::set)
                )))
    }
    override val aliases = listOf<String>()

    fun reload(context: CommandContext<CommandSourceStack>): Int {
        SkinManager.reload()
        return Command.SINGLE_SUCCESS
    }

    fun reset(context: CommandContext<CommandSourceStack>): Int {
        context.getArgument("player", PlayerSelectorArgumentResolver::class.java)
            .resolve(context.source).forEach(SkinManager::reset)
        return Command.SINGLE_SUCCESS
    }

    fun save(context: CommandContext<CommandSourceStack>): Int {
        SkinManager.save(
            context.getArgument("player", PlayerSelectorArgumentResolver::class.java).resolve(context.source).first(),
            StringArgumentType.getString(context, "skin")
        )
        return Command.SINGLE_SUCCESS
    }

    fun set(context: CommandContext<CommandSourceStack>): Int {
        context.getArgument("player", PlayerSelectorArgumentResolver::class.java)
            .resolve(context.source).forEach {
                SkinManager.set(it, StringArgumentType.getString(context, "skin"))
            }
        return Command.SINGLE_SUCCESS
    }

    fun <T : CommandSourceStack> isOp(context: T) = context.sender.isOp
}