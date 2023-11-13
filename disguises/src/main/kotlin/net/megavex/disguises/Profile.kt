package net.megavex.disguises

import java.util.*

public interface Profile {
    public val username: String
    public val uuid: UUID
    public val properties: Set<ProfileProperty>

    public fun modify(init: ProfileBuilder.() -> Unit): Profile {
        val builder = ProfileBuilder()
        builder.username = username
        builder.uuid = uuid
        builder.properties.addAll(properties)
        init(builder)
        return builder.build()
    }
}

internal data class CustomProfile(
    override val username: String,
    override val uuid: UUID,
    override val properties: Set<ProfileProperty>
) : Profile
