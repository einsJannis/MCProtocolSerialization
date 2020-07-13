package dev.einsjannis.minecraft.protocol.serialization.packet.`in`.handshake

import dev.einsjannis.minecraft.protocol.serialization.CommunicationState
import dev.einsjannis.minecraft.protocol.serialization.packet.Packet
import dev.einsjannis.minecraft.protocol.serialization.packet.PacketInformation
import dev.einsjannis.minecraft.protocol.serialization.type.VarInt
import kotlinx.serialization.Serializable

@Serializable
@PacketInformation(
        id = 0x00,
        state = CommunicationState.HANDSHAKING
)
data class Handshake(
        val protocolVersion: VarInt,
        val serverAddress: String,
        val serverPort: Short,
        val nextState: CommunicationState
) : Packet