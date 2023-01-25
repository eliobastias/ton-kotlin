package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.constructor.AnyTlbConstructor
import kotlin.jvm.JvmStatic

@Serializable
public data class Message<X>(
    val info: CommonMsgInfo,
    val init: Maybe<Either<StateInit, CellRef<StateInit>>>,
    val body: Either<X, CellRef<X>>
) : TlbObject {
    constructor(
        info: CommonMsgInfo,
        init: Pair<StateInit?, CellRef<StateInit>?>?,
        body: Pair<X?, CellRef<X>?>
    ) : this(info, init?.toEither().toMaybe(), body.toEither())

    constructor(
        info: CommonMsgInfo,
        init: StateInit?,
        body: X,
        storeInitInRef: Boolean = true,
        storeBodyInRef: Boolean = true
    ) : this(
        info = info,
        init = init?.let {
            if (storeInitInRef) null to CellRef(init) else init to null
        },
        body = if (storeBodyInRef) null to CellRef(body!!) else body!! to null
    )

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter {
        return printer.type("message") {
            field("info", info)
            field("init", init)
            field("body", body)
        }
    }

    companion object {
        val Any = tlbCodec(AnyTlbConstructor)

        @JvmStatic
        fun <X : Any> tlbCodec(
            x: TlbCodec<X>
        ): TlbConstructor<Message<X>> = MessageTlbConstructor(x)
    }

    override fun toString(): String = buildString {
        append("(message\n")
        append("info:")
        append(info)
        append(" init:")
        append(init)
        append(" body:")
        append(body.toString())
        append(")")
    }
}

operator fun <X : Any> Message.Companion.invoke(x: TlbCodec<X>) = tlbCodec(x)

private class MessageTlbConstructor<X : Any>(
    x: TlbCodec<X>
) : TlbConstructor<Message<X>>(
    schema = "message\$_ {X:Type} info:CommonMsgInfo " +
            "init:(Maybe (Either StateInit ^StateInit)) " +
            "body:(Either X ^X) = Message X;",
    id = BitString.empty()
) {
    private val eitherXX = Either(x, CellRef.tlbCodec(x))

    override fun storeTlb(
        cellBuilder: CellBuilder, value: Message<X>
    ) = cellBuilder {
        storeTlb(CommonMsgInfo, value.info)
        storeTlb(maybeEitherStateInitStateInit, value.init)
        storeTlb(eitherXX, value.body)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): Message<X> = cellSlice {
        val info = loadTlb(CommonMsgInfo)
        val init = loadTlb(maybeEitherStateInitStateInit)
        val body = loadTlb(eitherXX)
        Message(info, init, body)
    }

    companion object {
        private val maybeEitherStateInitStateInit =
            Maybe.tlbCodec(Either.tlbCodec(StateInit, CellRef.tlbCodec(StateInit)))
    }
}
