package net.megavex.disguises.plugin

import net.megavex.disguises.DisguiseProvider
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class NickCommand(private val provider: DisguiseProvider) : Command("nick") {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            return false
        }

        if (args.size != 1) {
            sender.sendPlainMessage("Usage: /nick <username>")
            return false
        }

        val username = args[0]
        sender.sendPlainMessage("Setting nick to: $username")
        provider.registerHook(sender.uniqueId, UsernameHook(username, UUID.randomUUID()))
        provider.refreshPlayer(sender)
        return true
    }
}