package net.megavex.disguises.util

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import net.megavex.disguises.ProfileHook
import net.megavex.disguises.Profile
import net.megavex.disguises.protocollib.WrappedProfile
import net.megavex.disguises.protocollib.WrappedProfileProperty
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket
import net.minecraft.world.phys.Vec3
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

internal class PacketProcessor(private val hookMap: Map<UUID, List<ProfileHook>>) : Listener {
    private val disguiseMap = mutableMapOf<Pair<UUID, Player>, UUID>()

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        disguiseMap.entries.removeIf { it.key.second == event.player }
    }

    fun processInfoUpdatePacket(
        player: Player,
        packet: ClientboundPlayerInfoUpdatePacket
    ): ClientboundPlayerInfoUpdatePacket? {
        val entries = packet.entries()

        val processedEntries = ArrayList<ClientboundPlayerInfoUpdatePacket.Entry>(entries.size)
        var hasChanged = false
        for (entry in entries) {
            val processed = processInfoData(player, entry)
            val final = if (processed != null) {
                hasChanged = true
                disguiseMap[entry.profileId to player] = processed.profileId
                processed
            } else {
                entry
            }

            processedEntries += final
        }

        return if (hasChanged) {
            ClientboundPlayerInfoUpdatePacket(packet.actions(), processedEntries)
        } else {
            null
        }
    }

    private fun processInfoData(
        viewer: Player,
        data: ClientboundPlayerInfoUpdatePacket.Entry
    ): ClientboundPlayerInfoUpdatePacket.Entry? {
        val hooks = hookMap[data.profileId] ?: return null

        var lastProfile: Profile = WrappedProfile(data.profile ?: return null)
        for (hook in hooks) {
            lastProfile = hook.onDisplay(viewer, lastProfile)
        }

        // Nothing changed
        if (lastProfile is WrappedProfile) {
            return null
        }

        val finalProfile = GameProfile(lastProfile.uuid, lastProfile.username)
        for (property in lastProfile.properties) {
            val inner = if (property is WrappedProfileProperty) {
                property.inner
            } else {
                Property(property.name, property.value, property.signature)
            }

            finalProfile.properties.put(property.name, inner)
        }

        return ClientboundPlayerInfoUpdatePacket.Entry(
            lastProfile.uuid,
            finalProfile,
            data.listed,
            data.latency,
            data.gameMode,
            data.displayName,
            data.chatSession
        )
    }

    fun onPlayerInfoRemovePacket(player: Player, packet: ClientboundPlayerInfoRemovePacket) {
        for (uuid in packet.profileIds) {
            disguiseMap.remove(uuid to player)
        }
    }

    fun processSpawnEntityPacket(player: Player, packet: ClientboundAddEntityPacket): ClientboundAddEntityPacket? {
        val disguisedUuid = disguiseMap[packet.uuid to player] ?: return null
        val velocity = Vec3(packet.xa, packet.ya, packet.za)
        return ClientboundAddEntityPacket(
            packet.id,
            disguisedUuid,
            packet.x,
            packet.y,
            packet.z,
            packet.xRot,
            packet.yRot,
            packet.type,
            packet.data,
            velocity,
            packet.yHeadRot.toDouble()
        )
    }
}