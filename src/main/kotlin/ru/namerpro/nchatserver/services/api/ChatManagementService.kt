package ru.namerpro.nchatserver.services.api

import ru.namerpro.nchatserver.model.Response

interface ChatManagementService {

    fun addNewChat(
        creatorId: Long,
        partnerId: Long,
        chatId: Long
    ): Response<Unit>

    fun newChats(
        clientId: Long
    ): Response<List<Triple<Long, Long, String>>>

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