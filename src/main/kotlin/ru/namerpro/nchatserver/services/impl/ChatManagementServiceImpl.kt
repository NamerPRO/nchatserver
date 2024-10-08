package ru.namerpro.nchatserver.services.impl

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.namerpro.nchatserver.model.Chat
import ru.namerpro.nchatserver.model.ChatData
import ru.namerpro.nchatserver.model.Response
import ru.namerpro.nchatserver.repositories.ClientRepository
import ru.namerpro.nchatserver.repositories.MessagesRepository
import ru.namerpro.nchatserver.repositories.NewChatsRepository
import ru.namerpro.nchatserver.services.api.ChatManagementService

@Service
class ChatManagementServiceImpl @Autowired constructor(
    private val clientRepository: ClientRepository,
    private val newChatsRepository: NewChatsRepository,
    private val messagesRepository: MessagesRepository
) : ChatManagementService {

    override fun newChats(
        clientId: Long
    ): Response<List<Chat>> {
        if (clientRepository.retrieve(clientId) == null) {
            return Response.FAILED()
        }
        val data = newChatsRepository.retrieve(clientId)
        newChatsRepository.delete(clientId)
        return Response.SUCCESS(data)
    }

    override fun isChatCreated(
        clientId: Long,
        chatId: Long
    ): Response<Boolean> {
        if (clientRepository.retrieve(clientId) == null) {
            return Response.FAILED()
        }
        val isCreated = messagesRepository.hasLinkageWith(clientId, chatId)
        return Response.SUCCESS(isCreated)
    }

    override fun createChat(
        creatorId: Long,
        partnerId: Long,
        chatData: ChatData
    ): Response<Long> {
        val client = clientRepository.retrieve(creatorId) ?: return Response.FAILED()
        val partner = clientRepository.retrieve(partnerId) ?: return Response.FAILED()

        val chatId = System.currentTimeMillis()
        val creatorToPartnerTopic = "topic_${creatorId}_${partnerId}_$chatId"
        val partnerToCreatorTopic = "topic_${partnerId}_${creatorId}_$chatId"

        messagesRepository.createTopics(listOf(
            NewTopic(creatorToPartnerTopic, STANDARD_NUMBER_OF_PARTITIONS, STANDARD_REPLICA_FACTOR),
            NewTopic(partnerToCreatorTopic, STANDARD_NUMBER_OF_PARTITIONS, STANDARD_REPLICA_FACTOR)
        ))

        client.chatIdToPartner[chatId] = partnerId
        partner.chatIdToPartner[chatId] = creatorId

        messagesRepository.createChat(creatorId, chatId, partnerToCreatorTopic)
        messagesRepository.createChat(partnerId, chatId, creatorToPartnerTopic)

        newChatsRepository.store(partnerId, Chat(chatData.chatName, chatId, client.name, creatorId, chatData.secret, chatData.cipherType, chatData.iv))

        return Response.SUCCESS(chatId)
    }

    override fun leaveChat(
        clientId: Long,
        chatId: Long
    ): Response<Unit> {
        val client = clientRepository.retrieve(clientId) ?: return Response.FAILED()
        val partnerId = client.chatIdToPartner[chatId] ?: return Response.FAILED()

        messagesRepository.deleteChat(clientId, chatId)

        // If partner did not disconnect
        if (messagesRepository.hasLinkageWith(partnerId, chatId)) {
            // Let him know we did
            messagesRepository.sendMessage(clientId, Pair(chatId, "${MessagesRepository.EXIT_CODE}"))
        }

        client.chatIdToPartner.remove(chatId)

        val partnerToClientTopic = "topic_${partnerId}_${clientId}_$chatId"
        messagesRepository.removeTopics(listOf(partnerToClientTopic))

        if (clientId == partnerId) {
            val clientToPartnerTopic = "topic_${clientId}_${partnerId}_$chatId"
            messagesRepository.removeTopics(listOf(clientToPartnerTopic))
        }

        return Response.SUCCESS()
    }

    companion object {
        private const val STANDARD_NUMBER_OF_PARTITIONS = 1
        private const val STANDARD_REPLICA_FACTOR: Short = 1
    }

}