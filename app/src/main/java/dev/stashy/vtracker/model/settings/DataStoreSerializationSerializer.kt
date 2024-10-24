package dev.stashy.vtracker.model.settings

import androidx.datastore.core.Serializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.serializer
import java.io.InputStream
import java.io.OutputStream

class DataStoreSerializationSerializer<T>(
    override val defaultValue: T,
    val serializer: KSerializer<T>,
    val json: Json = dev.stashy.vtracker.model.settings.json
) : Serializer<T> {
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): T =
        json.decodeFromStream(serializer, input)

    override suspend fun writeTo(t: T, output: OutputStream) {
        output.write(json.encodeToString(serializer, t).encodeToByteArray())
    }
}

inline fun <reified T> dataStoreSerializer(
    defaultValue: T,
    json: Json = dev.stashy.vtracker.model.settings.json
) = DataStoreSerializationSerializer(defaultValue, serializer<T>(), json)
