package org.ton.api.http

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.encodeHex
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*

@SerialName("http.payloadPart")
@Serializable
data class HttpPayloadPart(
    @Serializable(HexByteArraySerializer::class)
    val data: ByteArray,
    val trailer: List<HttpHeader>,
    val last: Boolean
) {
    companion object : TlCodec<HttpPayloadPart> by HttpPayloadPartTlConstructor

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HttpPayloadPart) return false

        if (!data.contentEquals(other.data)) return false
        if (trailer != other.trailer) return false
        if (last != other.last) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + trailer.hashCode()
        result = 31 * result + last.hashCode()
        return result
    }

    override fun toString(): String = "HttpPayloadPart(data=${data.encodeHex()}, trailer=$trailer, last=$last)"
}

private object HttpPayloadPartTlConstructor : TlConstructor<HttpPayloadPart>(
    type = HttpPayloadPart::class,
    schema = "http.payloadPart data:bytes trailer:(vector http.Header) last:Bool = http.PayloadPart"
) {
    override fun decode(input: Input): HttpPayloadPart {
        val data = input.readBytesTl()
        val trailer = input.readVectorTl(HttpHeader)
        val last = input.readBoolTl()
        return HttpPayloadPart(data, trailer, last)
    }

    override fun encode(output: Output, value: HttpPayloadPart) {
        output.writeBytesTl(value.data)
        output.writeVectorTl(value.trailer, HttpHeader)
        output.writeBoolTl(value.last)
    }
}