package net.megavex.disguises.protocollib

import com.comphenix.protocol.ProtocolLibrary
import net.megavex.disguises.ProfileHook
import net.megavex.disguises.DisguiseProvider
import net.megavex.disguises.util.PacketProcessor
import net.megavex.disguises.util.SelfPlayerRefreshUtil
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.plugin.Plugin
import java.util.*

internal class ProtocolLibDisguiseProvider(private val plugin: Plugin) : DisguiseProvider {
    private val hookMap = mutableMapOf<UUID, MutableList<ProfileHook>>()
    private val processor = PacketProcessor(hookMap)
    private val packetListener = DisguisePacketListener(plugin, processor)
    private var isClosed = false

    fun register() {
        plugin.server.pluginManager.registerEvents(processor, plugin)
        ProtocolLibrary.getProtocolManager().addPacketListener(packetListener)
    }

    override fun registerHook(playerUuid: UUID, hook: ProfileHook) {
        val hooks = hookMap.computeIfAbsent(playerUuid) { ArrayList(1) }
        if (hook !in hooks) {
            hooks += hook
        }
    }

    override fun unregisterHook(playerUuid: UUID, hook: ProfileHook) {
        val hooks = hookMap[playerUuid] ?: return
        if (hooks.remove(hook) && hooks.isEmpty()) {
            hookMap.remove(playerUuid)
        }
    }

    override fun refreshPlayer(player: Player) {
        val viewers = ProtocolLibrary.getProtocolManager().getEntityTrackers(player)
        viewers += player
        refreshPlayer(player, viewers)
    }

    override fun refreshPlayer(player: Player, viewers: Collection<Player>) {
        for (viewer in viewers) {
            if (viewer == player) {
                SelfPlayerRefreshUtil.refresh(player)
                continue
            }

            if (viewer.canSee(player)) {
                // TODO: this fires events, we might not want that to happen
                viewer.hidePlayer(plugin, player)
                viewer.showPlayer(plugin, player)
            }
        }
    }

    override fun close() {
        if (!isClosed) {
            isClosed = true
            HandlerList.unregisterAll(processor)
            ProtocolLibrary.getProtocolManager().removePacketListener(packetListener)
            // TODO: remove disguises
        }
    }
}