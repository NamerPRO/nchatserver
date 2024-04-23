package ru.namerpro.nchatserver.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.namerpro.nchatserver.model.Response
import ru.namerpro.nchatserver.repositories.ClientRepository
import ru.namerpro.nchatserver.repositories.MessagesRepository
import ru.namerpro.nchatserver.services.api.MessageTransferService

@Service
class MessageTransferServiceImpl @Autowired constructor(
    private val clientRepository: ClientRepository,
    private val messagesRepository: MessagesRepository
) : MessageTransferService {

    override fun getMessages(
        clientId: Long,
        chatId: Long
    ): Response<List<String>> {
        val client = clientRepository.retrieve(clientId)
        if (client == null || !client.chatIdToPartner.containsKey(chatId)) {
            return Response.FAILED()
        }
        val messages = messagesRepository.receiveMessages(clientId, chatId)
        return Response.SUCCESS(messages)
    }

    override fun sendMessage(
        clientId: Long,
        chatId: Long,
        message: String
    ): Response<Unit> {
        val client = clientRepository.retrieve(clientId)
        val partnerId = client?.chatIdToPartner?.get(chatId) ?: -1
        if (client == null || !messagesRepository.hasLinkageWith(partnerId, chatId)) {
            return Response.FAILED()
        }
        messagesRepository.sendMessage(clientId, Pair(chatId, "${MessagesRepository.MESSAGE_CODE}$message"))
        return Response.SUCCESS()
    }

}