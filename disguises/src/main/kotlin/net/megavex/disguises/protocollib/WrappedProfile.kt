package net.megavex.disguises.protocollib

import com.mojang.authlib.GameProfile
import net.megavex.disguises.Profile
import net.megavex.disguises.ProfileProperty
import java.util.*

internal class WrappedProfile(private val inner: GameProfile) : Profile {
    override val username: String
        get() = inner.name
    override val uuid: UUID
        get() = inner.id

    override val properties: Set<ProfileProperty> by lazy {
        inner.properties.values().map { WrappedProfileProperty(it) }.toSet()
    }

    override fun equals(other: Any?): Boolean {
        return inner == (other as? WrappedProfile)?.inner
    }

    override fun hashCode(): Int {
        return inner.hashCode()
    }

    override fun toString(): String {
        return "WrappedProfile(username='$username', uuid=$uuid, properties=$properties)"
    }
}