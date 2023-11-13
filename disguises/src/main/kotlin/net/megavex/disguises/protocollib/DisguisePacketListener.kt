package net.megavex.disguises.protocollib

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import net.megavex.disguises.util.PacketProcessor
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket
import org.bukkit.plugin.Plugin

internal class DisguisePacketListener(
    plugin: Plugin,
    private val processor: PacketProcessor
) : PacketAdapter(
    plugin,
    PacketType.Play.Server.PLAYER_INFO,
    PacketType.Play.Server.PLAYER_INFO_REMOVE,
    PacketType.Play.Server.SPAWN_ENTITY
) {
    override fun onPacketSending(event: PacketEvent) {
        when (event.packetType) {
            PacketType.Play.Server.PLAYER_INFO -> {
                val newPacket = processor.processInfoUpdatePacket(event.player, event.packet.handle as ClientboundPlayerInfoUpdatePacket)
                if (newPacket != null) {
                    event.packet = PacketContainer.fromPacket(newPacket)
                }
            }
            PacketType.Play.Server.PLAYER_INFO_REMOVE -> {
                processor.onPlayerInfoRemovePacket(event.player, event.packet.handle as ClientboundPlayerInfoRemovePacket)
            }
            PacketType.Play.Server.SPAWN_ENTITY -> {
                val newPacket = processor.processSpawnEntityPacket(event.player, event.packet.handle as ClientboundAddEntityPacket)
                if (newPacket != null) {
                    event.packet = PacketContainer.fromPacket(newPacket)
                }
            }
        }
    }
}