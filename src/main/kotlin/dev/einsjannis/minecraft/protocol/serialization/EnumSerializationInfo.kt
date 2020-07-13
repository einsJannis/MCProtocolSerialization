package dev.einsjannis.minecraft.protocol.serialization

@Target(AnnotationTarget.ANNOTATION_CLASS)
//@RequiresOptIn(level = RequiresOptIn.Level.WARNING)
annotation class EnumSerializationInfo(val type: Type) {
    enum class Type {
        BYTE, VARINT, INT, STRING
    }
}