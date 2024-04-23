package ru.namerpro.nchatserver.services.api

import ru.namerpro.nchatserver.model.Chat
import ru.namerpro.nchatserver.model.ChatData
import ru.namerpro.nchatserver.model.Response

interface ChatManagementService {

    fun newChats(
        clientId: Long
    ): Response<List<Chat>>

    fun isChatCreated(
        clientId: Long,
        chatId: Long
    ): Response<Boolean>

    fun createChat(
        creatorId: Long,
        partnerId: Long,
        chatData: ChatData
    ): Response<Long>

    fun leaveChat(
        clientId: Long,
        chatId: Long
    ): Response<Unit>

}