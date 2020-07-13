package dev.einsjannis.minecraft.protocol.serialization.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class JSONChat(
        @SerialName("text") val text: String,
        @SerialName("bold") val bold: Boolean = false,
        @SerialName("italic") val italic: Boolean = false,
        @SerialName("underlined") val underlined: Boolean = false,
        @SerialName("strikethrough") val strikethrough: Boolean = false,
        @SerialName("color") val color: String = "white",
        @SerialName("insertion") val insertion: String? = null,
        @SerialName("clickEvent") val clickEvent: JSONChatEvent? = null,
        @SerialName("hoverEvent") val hoverEvent: JSONChatEvent? = null,
        @SerialName("extra") val extra: List<JSONChat>? = null
)

@Serializable
class JSONChatEvent (
        val action: String,
        val value: String
) {
    fun toMap(): Map<String, String> = mapOf(
            "action" to action
    )
}