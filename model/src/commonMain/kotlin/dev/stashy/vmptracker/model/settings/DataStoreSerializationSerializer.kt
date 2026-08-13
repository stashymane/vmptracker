package dev.stashy.vmptracker.model.settings

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.serializer
import java.io.InputStream
import java.io.OutputStream

class DataStoreSerializationSerializer<T>(
    override val defaultValue: T,
    val serializer: KSerializer<T>,
    val json: Json = dev.stashy.vmptracker.model.json
) : Serializer<T> {
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): T =
        try {
            json.decodeFromStream(serializer, input)
        } catch (e: SerializationException) {
            throw CorruptionException("Cannot read settings", e)
        } catch (e: IllegalArgumentException) {
            throw CorruptionException("Cannot read settings", e)
        }

    override suspend fun writeTo(t: T, output: OutputStream) = withContext(Dispatchers.IO) {
        output.write(json.encodeToString(serializer, t).encodeToByteArray())
    }
}

inline fun <reified T> dataStoreSerializer(
    defaultValue: T,
    json: Json = dev.stashy.vmptracker.model.json
) = DataStoreSerializationSerializer(defaultValue, serializer<T>(), json)
