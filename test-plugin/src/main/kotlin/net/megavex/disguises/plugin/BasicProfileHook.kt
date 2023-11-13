package net.megavex.disguises.plugin

import net.megavex.disguises.Profile
import net.megavex.disguises.ProfileHook
import org.bukkit.entity.Player
import java.util.*

class BasicProfileHook(private val username: String, private val uuid: UUID) : ProfileHook {
    override fun onDisplay(viewer: Player, profile: Profile): Profile {
        return profile.modify {
            username = this@BasicProfileHook.username
            uuid = this@BasicProfileHook.uuid
            properties.clear()
        }
    }
}