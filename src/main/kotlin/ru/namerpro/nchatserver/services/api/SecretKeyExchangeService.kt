package ru.namerpro.nchatserver.services.api

import ru.namerpro.nchatserver.model.Response
import ru.namerpro.nchatserver.model.Secret

interface SecretKeyExchangeService {

    fun sendPartOfKey(
        receiverId: Long,
        chatId: Long,
        partOfKey: String
    ): Response<Unit>

    fun getPartsOfKeys(
        clientId: Long
    ): Response<List<Secret>>

}