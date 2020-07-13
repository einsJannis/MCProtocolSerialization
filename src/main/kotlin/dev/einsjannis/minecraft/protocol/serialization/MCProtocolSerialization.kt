package dev.einsjannis.minecraft.protocol.serialization

import dev.einsjannis.minecraft.protocol.serialization.packet.PacketInformation
import dev.einsjannis.minecraft.protocol.serialization.type.VarInt
import dev.einsjannis.minecraft.protocol.serialization.type.toVarInt
import kotlinx.io.ByteArrayOutputStream
import kotlinx.serialization.*
import kotlinx.serialization.builtins.AbstractEncoder
import kotlinx.serialization.modules.SerialModule

class MCProtocolSerialization(override val context: SerialModule, val communicationState: CommunicationState) : BinaryFormat {

    @OptIn(InternalSerializationApi::class)
    inner class MCProtocolEncoder(val output: ByteArrayOutputStream, val isPacket: Boolean) : AbstractEncoder() {
        override val context: SerialModule
            get() = this@MCProtocolSerialization.context

        override fun beginStructure(
                descriptor: SerialDescriptor,
                vararg typeSerializers: KSerializer<*>
        ): CompositeEncoder {
            if (isPacket) {
                val packetInformation = descriptor.annotations.find { it is PacketInformation } as PacketInformation?
                if (!descriptor.kind.isLegal() || packetInformation.isLegal())
                    throw SerializationException("Only valid packets are serializable")
                output.writeBytes(packetInformation!!.id.toVarInt().toByteArray())
            }
            return MCProtocolEncoder(output, false)
        }
        private fun SerialKind.isLegal(): Boolean {
            return this == StructureKind.CLASS || this == StructureKind.OBJECT || this is PolymorphicKind
        }
        private fun PacketInformation?.isLegal(): Boolean {
            return this != null && this.state == communicationState
        }
        override fun encodeBoolean(value: Boolean) = output.writeBytes(value.toByteArray())
        override fun encodeByte(value: Byte) = output.writeBytes(value.toByteArray())
        override fun encodeShort(value: Short) = output.writeBytes(value.toByteArray())
        override fun encodeInt(value: Int) = output.writeBytes(value.toByteArray())
        override fun encodeLong(value: Long) = output.writeBytes(value.toByteArray())
        override fun encodeFloat(value: Float) = output.writeBytes(value.toByteArray())
        override fun encodeDouble(value: Double) = output.writeBytes(value.toByteArray())
        override fun encodeChar(value: Char) = output.writeBytes(value.toByteArray())
        override fun encodeString(value: String) {
            MCProtocolEncoder(output, false).encode(VarInt.serializer(), value.length.toVarInt())
            output.writeBytes(value.toByteArray(Charsets.UTF_8))
        }
        override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
            val typeAnnotation = enumDescriptor.annotations.find {it is EnumSerializationInfo} as EnumSerializationInfo?
            when (typeAnnotation?.type ?: EnumSerializationInfo.Type.VARINT) {
                EnumSerializationInfo.Type.BYTE -> encodeByte(index.toByte())
                EnumSerializationInfo.Type.VARINT ->
                    MCProtocolEncoder(output, false).encode(VarInt.serializer(), index.toVarInt())
                EnumSerializationInfo.Type.INT -> encodeInt(index)
                EnumSerializationInfo.Type.STRING -> encodeString(enumDescriptor.serialName)
            }
        }
        override fun encodeNull() = throw SerializationException("Null can't be serialized")
        override fun encodeUnit() = throw SerializationException("Unit can't be serialized")
        override fun endStructure(descriptor: SerialDescriptor) = Unit
    }

    override fun <T> dump(serializer: SerializationStrategy<T>, value: T): ByteArray {
        val output = ByteArrayOutputStream()
        val encoder = MCProtocolEncoder(output, true)
        encoder.encode(serializer, value)
        return output.toByteArray()
    }

    override fun <T> load(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
        TODO()
    }
}

fun Boolean.toByteArray() = if (this) byteArrayOf(0x01) else byteArrayOf(0x00)
fun Byte.toByteArray() = byteArrayOf(this)
fun Short.toByteArray(): ByteArray = byteArrayOf(this.toByte(), (this.toInt() shr 8).toByte()).reversedArray()
fun Int.toByteArray(): ByteArray = byteArrayOf(
        this.toByte(),
        (this shr 8).toByte(),
        (this shr 16).toByte(),
        (this shr 24).toByte()
).reversedArray()
fun Long.toByteArray(): ByteArray = byteArrayOf(
        this.toByte(),
        (this shr 8).toByte(),
        (this shr 16).toByte(),
        (this shr 24).toByte(),
        (this shr 32).toByte(),
        (this shr 40).toByte(),
        (this shr 48).toByte(),
        (this shr 56).toByte()
).reversedArray()
fun Float.toByteArray(): ByteArray = TODO()
fun Double.toByteArray(): ByteArray = TODO()
fun Char.toByteArray(): ByteArray = byteArrayOf(this.toByte())
