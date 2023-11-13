package net.megavex.disguises.protocollib

import com.mojang.authlib.properties.Property
import net.megavex.disguises.ProfileProperty

internal class WrappedProfileProperty(val inner: Property) : ProfileProperty {
    override val name: String
        get() = inner.name
    override val value: String
        get() = inner.value
    override val signature: String
        get() = inner.signature ?: error("signature not found")

    override fun equals(other: Any?): Boolean {
        return inner == (other as? WrappedProfileProperty)?.inner
    }

    override fun hashCode(): Int {
        return inner.hashCode()
    }

    override fun toString(): String {
        return "WrappedProfileProperty(name='$name', value='$value', signature='$signature')"
    }
}
