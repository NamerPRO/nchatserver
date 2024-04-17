package ru.namerpro.nchatserver.services.api

import ru.namerpro.nchatserver.model.Response

interface ChatManagementService {

    fun requestChat(
        creatorId: Long,
        partnerId: Long
    ): Response<Unit>

    fun pingChatRequest(
        clientId: Long
    ): Response<List<Long>>

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