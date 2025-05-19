package com.postopia.data.remote.util

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import java.lang.reflect.Type

/**
 * Custom Moshi JsonAdapter for Kotlin's Unit type.
 * This adapter handles deserialization of any JSON value (including null or an empty object)
 * into Kotlin's Unit object, and serializes Unit to JSON null.
 * This helps Moshi correctly create converters for generic types like ApiResponse<Unit>.
 */
class UnitJsonAdapter : JsonAdapter<Unit>() {
    override fun fromJson(reader: JsonReader): Unit {
        // Consumes the JSON value, whatever it might be (null, empty object, etc.),
        // and returns the singleton Unit object.
        reader.skipValue()
        return Unit
    }

    override fun toJson(writer: JsonWriter, value: Unit?) {
        // Represents Unit as JSON null when serializing.
        writer.nullValue()
    }

    companion object {
        val FACTORY = object : Factory {
            override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
                // Check if the type is kotlin.Unit.
                // Note: Using Unit::class.java might return java.lang.Void.TYPE on some JVMs for Unit.
                // So, checking against both Unit::class.java and java.lang.Void.TYPE can be more robust
                // if issues arise, but typically Unit::class.java should work for Kotlin's Unit.
                if (type == Unit::class.java || type.typeName == "kotlin.Unit") {
                    return UnitJsonAdapter()
                }
                return null
            }
        }
    }
}

