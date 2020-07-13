package dev.einsjannis.minecraft.protocol.serialization.packet

import dev.einsjannis.minecraft.protocol.serialization.CommunicationState

annotation class PacketInformation(
        val id: Int,
        val state: CommunicationState
)