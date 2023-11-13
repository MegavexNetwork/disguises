package net.megavex.disguises

import java.util.*
import kotlin.collections.HashSet

public class ProfileBuilder {
    public lateinit var username: String
    public lateinit var uuid: UUID
    public val properties: MutableSet<ProfileProperty> = HashSet(1)

    public fun addProperty(name: String, value: String, signature: String) {
        properties += CustomProperty(name, value, signature)
    }

    internal fun build(): Profile {
        return CustomProfile(username, uuid, properties.toSet())
    }
}

public fun Profile(init: ProfileBuilder.() -> Unit): Profile {
    val builder = ProfileBuilder()
    init(builder)
    return builder.build()
}
