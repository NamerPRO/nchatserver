package ru.namerpro.nchatserver.services.api

import ru.namerpro.nchatserver.model.Response

interface MessageTransferService {

    fun getMessages(
        clientId: Long,
        chatId: Long
    ): Response<List<String>>

    fun sendMessage(
        clientId: Long,
        chatId: Long,
        message: String
    ): Response<Unit>

}