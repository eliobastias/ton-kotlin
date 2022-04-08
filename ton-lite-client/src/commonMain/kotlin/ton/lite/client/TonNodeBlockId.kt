package ton.lite.client

import io.ktor.utils.io.core.*
import ton.adnl.TLCodec

data class TonNodeBlockId(
    val workchain: Int,
    val shard: Long,
    val seqno: Int
) {
    companion object : TLCodec<TonNodeBlockId> {
        override val id: Int = -1211256473

        override fun decode(input: Input): TonNodeBlockId {
            val workchain = input.readIntLittleEndian()
            val shard = input.readLongLittleEndian()
            val seqno = input.readIntLittleEndian()
            return TonNodeBlockId(workchain, shard, seqno)
        }

        override fun encode(output: Output, message: TonNodeBlockId) {
            output.writeIntLittleEndian(message.workchain)
            output.writeLongLittleEndian(message.shard)
            output.writeIntLittleEndian(message.seqno)
        }
    }
}