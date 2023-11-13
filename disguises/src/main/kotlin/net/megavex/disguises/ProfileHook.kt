package net.megavex.disguises

import org.bukkit.entity.Player

public interface ProfileHook {
    public fun onDisplay(viewer: Player, profile: Profile): Profile
}
