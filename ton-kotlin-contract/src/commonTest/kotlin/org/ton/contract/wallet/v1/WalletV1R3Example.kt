package org.ton.contract.wallet.v1

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.contract.wallet.liteClient
import org.ton.crypto.hex

private val privateKey = PrivateKeyEd25519(ByteArray(32))

suspend fun main() {
    val liteClient = liteClient()
    val wallet = ContractV1R3(liteClient.liteApi, privateKey)
    val address = wallet.address()
    println("Source wallet address = ${address.toString(userFriendly = false)}")
    println("Non-bounceable address (for init only): ${address.toString(bounceable = false, testOnly = true)}")
    println("Bounceable address (for later access): ${address.toString(bounceable = true, testOnly = true)}")
    println("Corresponding public key is ${hex(wallet.privateKey.publicKey().key).uppercase()}")

    val block = liteClient.liteApi.getMasterchainInfo().last

    println("seqno: ${wallet.seqno(block)}")
    println("get_public_key: ${wallet.getPublicKey(block)}")
}