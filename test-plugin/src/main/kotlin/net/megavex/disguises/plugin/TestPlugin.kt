package net.megavex.disguises.plugin

import net.megavex.disguises.DisguiseProvider
import org.bukkit.command.Command
import org.bukkit.plugin.java.JavaPlugin

@Suppress("UNUSED")
class TestPlugin : JavaPlugin() {
    private lateinit var provider: DisguiseProvider

    override fun onEnable() {
        provider = DisguiseProvider.protocolLib(this)
        server.commandMap.register("nick", NickCommand(provider))
    }

    override fun onDisable() {
        provider.close()
    }
}