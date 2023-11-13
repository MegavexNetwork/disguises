package net.megavex.disguises

public interface ProfileProperty {
    public val name: String
    public val value: String
    public val signature: String
}

internal data class CustomProperty(
    override val name: String,
    override val value: String,
    override val signature: String
) : ProfileProperty
