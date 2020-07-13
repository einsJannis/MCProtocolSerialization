package dev.einsjannis.minecraft.protocol.serialization.type

import kotlinx.serialization.Serializable

@Serializable
class Angle(val byte: Byte) {
    fun toByteArray(): ByteArray {
        return byteArrayOf(byte)
    }
}