package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor

@Serializable
@SerialName("ed25519_signature")
data class CryptoSignatureSimple(
    val r: BitString,
    val s: BitString
) : CryptoSignature {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<CryptoSignatureSimple> = CryptoSignatureSimpleTlbConstructor
    }
}

private object CryptoSignatureSimpleTlbConstructor : TlbConstructor<CryptoSignatureSimple>(
    schema = "ed25519_signature#5 R:bits256 s:bits256 = CryptoSignatureSimple;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: CryptoSignatureSimple
    ) = cellBuilder {
        storeBits(value.r)
        storeBits(value.s)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): CryptoSignatureSimple = cellSlice {
        val r = loadBitString(256)
        val s = loadBitString(256)
        CryptoSignatureSimple(r, s)
    }
}