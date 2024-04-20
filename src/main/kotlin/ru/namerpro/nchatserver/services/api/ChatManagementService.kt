package ru.namerpro.nchatserver.services.api

import ru.namerpro.nchatserver.model.Response

interface ChatManagementService {

    fun addNewChat(
        creatorId: Long,
        partnerId: Long,
        chatId: Long,
        secret: String,
        chatName: String
    ): Response<Unit>

    fun newChats(
        clientId: Long
    ): Response<List<Triple<Pair<Long, String>, Pair<Long, String>, String>>>

    fun isChatCreated(
        clientId: Long,
        chatId: Long
    ): Response<Boolean>

    fun createChat(
        creatorId: Long,
        partnerId: Long
    ): Response<Long>

    fun leaveChat(
        clientId: Long,
        chatId: Long
    ): Response<Unit>

}