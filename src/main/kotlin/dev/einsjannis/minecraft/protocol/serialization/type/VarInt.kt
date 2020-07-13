package dev.einsjannis.minecraft.protocol.serialization.type

import kotlinx.io.ByteArrayInputStream
import kotlinx.io.ByteArrayOutputStream
import kotlinx.serialization.Serializable

@Serializable
data class VarInt(private val byteArray: ByteArray) {

    init {
        if (byteArray.size > 5) throw RuntimeException("VarInt is too big")
    }

    fun toByteArray(): ByteArray {
        return byteArray
    }

    fun toInt(): Int {
        val byteArrayInputStream = ByteArrayInputStream(byteArray)
        var numRead = 0
        var result = 0
        var read: Int
        do {
            read = byteArrayInputStream.read()
            val value: Int = read and 127
            result = result or (value shl 7 * numRead)
            numRead++
            if (numRead > 5) {
                throw RuntimeException("VarInt is too big")
            }
        } while (read and 128 != 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is VarInt -> other.toInt() == toInt()
            is Int -> other == toInt()
            is ByteArray -> other == toByteArray()
            else -> false
        }
    }

    override fun toString(): String {
        return toInt().toString()
    }

    override fun hashCode(): Int {
        return byteArray.contentHashCode()
    }

}

fun Int.toVarInt(): VarInt {
    val byteArrayOutputStream = ByteArrayOutputStream()
    var value = this
    do {
        var temp = (value and 127)
        value = value ushr 7
        if (value != 0) {
            temp = temp or 128
        }
        byteArrayOutputStream.write(temp)
    } while (value != 0)
    return VarInt(byteArrayOutputStream.toByteArray())
}