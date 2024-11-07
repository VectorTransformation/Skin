package skin.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import skin.data.FileData
import skin.system.SkinSystem

class SkinCommand : TabExecutor {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String> {
        return when (args.size) {
            1 -> listOf(
                "reload",
                "reset",
                "save",
                "set"
            )
            2 -> {
                when (args[0]) {
                    "reset", "save", "set" -> Bukkit.getOnlinePlayers().map {
                        it.name
                    }
                    else -> listOf()
                }
            }
            3 -> {
                when (args[0]) {
                    "save" -> listOf(
                        "tmp"
                    )
                    "set" -> SkinSystem.skinList()
                    else -> listOf()
                }
            }
            else -> listOf()
        }
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): Boolean {
        when (args.size) {
            1 -> when (args[0]) {
                "reload" -> reload(sender, args)
            }
            2 -> when (args[0]) {
                "reset" -> reset(sender, args)
                "save" -> save(sender, args)
            }
            3 -> when (args[0]) {
                "save" -> save(sender, args)
                "set" -> set(sender, args)
            }
        }
        return true
    }

    fun reload(sender: CommandSender, args: Array<String>) {
        FileData.reloader(sender as? Player)
    }

    fun reset(sender: CommandSender, args: Array<String>) {
        val player = Bukkit.getPlayer(args[1]) ?: return
        SkinSystem.reset(player)
    }

    fun save(sender: CommandSender, args: Array<String>) {
        val player = Bukkit.getPlayer(args[1]) ?: return
        val name = if (args.size == 3) {
            args[2]
        } else {
            "tmp"
        }
        SkinSystem.save(player, name)
    }

    fun set(sender: CommandSender, args: Array<String>) {
        val player = Bukkit.getPlayer(args[1]) ?: return
        val name = args[2]
        SkinSystem.set(player, name)
    }
}