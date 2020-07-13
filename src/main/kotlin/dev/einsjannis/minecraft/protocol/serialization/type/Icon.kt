package dev.einsjannis.minecraft.protocol.serialization.type

import kotlinx.serialization.Serializable

@Serializable
class Icon(
        val type: VarInt,
        val x: Byte,
        val y: Byte,
        val direction: Byte,
        val hasDisplayName: Boolean,
        val displayName: JSONChat?
)
