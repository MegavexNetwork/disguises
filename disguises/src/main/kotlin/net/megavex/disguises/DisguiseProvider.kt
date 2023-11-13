package net.megavex.disguises

import net.megavex.disguises.protocollib.ProtocolLibDisguiseProvider
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.UUID

public interface DisguiseProvider {
    public companion object {
        public fun protocolLib(plugin: Plugin): DisguiseProvider {
            return ProtocolLibDisguiseProvider(plugin)
                .also { it.register() }
        }
    }

    public fun registerHook(playerUuid: UUID, hook: ProfileHook)

    public fun unregisterHook(playerUuid: UUID, hook: ProfileHook)

    public fun refreshPlayer(player: Player)

    public fun refreshPlayer(player: Player, viewers: Collection<Player>)

    public fun close()
}