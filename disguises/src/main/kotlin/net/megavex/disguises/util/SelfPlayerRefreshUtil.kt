package net.megavex.disguises.util

import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer
import org.bukkit.entity.Player

internal object SelfPlayerRefreshUtil {
    private val CB_PACKAGE = Bukkit.getServer().javaClass.`package`.name
    private val REFRESH_METHOD = Class.forName("$CB_PACKAGE.entity.CraftPlayer")
        .getDeclaredMethod("refreshPlayer")

    init {
        REFRESH_METHOD.isAccessible = true
    }

    fun refresh(player: Player) {
        val handle = (player as CraftPlayer).handle
        handle.connection.send(ClientboundPlayerInfoRemovePacket(listOf(player.uniqueId)))
        handle.connection.send(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(listOf(handle), handle))
        REFRESH_METHOD.invoke(player)
    }
}