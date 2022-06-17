package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor

@Serializable
@SerialName("fsm_split")
data class FutureSplitMergeSplit(
    val split_utime: Long,
    val interval: Long
) : FutureSplitMerge {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<FutureSplitMergeSplit> = FutureSplitMergeSplitTlbConstructor
    }
}

private object FutureSplitMergeSplitTlbConstructor : TlbConstructor<FutureSplitMergeSplit>(
    schema = "fsm_merge\$11 merge_utime:uint32 interval:uint32 = FutureSplitMerge;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: FutureSplitMergeSplit
    ) = cellBuilder {
        storeUInt(value.split_utime, 32)
        storeUInt(value.interval, 32)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): FutureSplitMergeSplit = cellSlice {
        val splitUtime = loadUInt(32).toLong()
        val interval = loadUInt(32).toLong()
        FutureSplitMergeSplit(splitUtime, interval)
    }
}