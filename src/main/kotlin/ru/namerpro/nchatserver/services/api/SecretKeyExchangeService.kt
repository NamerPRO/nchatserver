package ru.namerpro.nchatserver.services.api

import ru.namerpro.nchatserver.model.Response

interface SecretKeyExchangeService {

    fun sendPartOfKey(
        receiverId: Long,
        chatId: Long,
        partOfKey: String
    ): Response<Unit>

    fun getPartsOfKeys(
        clientId: Long
    ): Response<List<Pair<Long, String>>>

}