package ru.namerpro.nchatserver.services.api

import ru.namerpro.nchatserver.model.Response
import java.math.BigInteger

interface SecretKeyExchangeService {

    fun sendPartOfKey(
        senderId: Long,
        receiverId: Long,
        partOfKey: BigInteger
    ): Response<Unit>

    fun getPartsOfKeys(
        clientId: Long
    ): Response<List<Pair<Long, BigInteger>>>

}