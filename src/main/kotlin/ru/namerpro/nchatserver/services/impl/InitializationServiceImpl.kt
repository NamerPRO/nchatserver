package ru.namerpro.nchatserver.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.namerpro.nchatserver.model.Client
import ru.namerpro.nchatserver.model.Response
import ru.namerpro.nchatserver.repositories.ChatRequestsRepository
import ru.namerpro.nchatserver.repositories.ClientRepository
import ru.namerpro.nchatserver.repositories.MessagesRepository
import ru.namerpro.nchatserver.repositories.SecretKeyRepository
import ru.namerpro.nchatserver.services.api.InitializationService
import java.util.concurrent.atomic.AtomicLong

@Service
class InitializationServiceImpl @Autowired constructor(
    private val clientRepository: ClientRepository,
    private val chatRequestsRepository: ChatRequestsRepository,
    private val secretKeyRepository: SecretKeyRepository,
    private val messagesRepository: MessagesRepository
): InitializationService {

    private val clientIdHolder = AtomicLong()

    override fun initialize(
        clientName: String
    ): Response<Long> {
        if (clientName.isBlank()) {
            return Response.FAILED()
        }
        val clientId = clientIdHolder.incrementAndGet()
        clientRepository.store(clientId, Client(clientName))
        return Response.SUCCESS(clientId)
    }

    override fun deinitialize(
        clientId: Long
    ): Response<Unit> {
        val client = clientRepository.retrieve(clientId) ?: return Response.FAILED()

        // Telling every client we left
        client.chatIdToPartner.forEach {
            val partnerId = it.value
            val chatId = it.key

            val topicPartnerToClient = "topic_${partnerId}_${clientId}_$chatId"

            if (clientRepository.retrieve(partnerId) != null) {
                // Telling client we left, because it is not disconnected
                messagesRepository.sendMessage(clientId, Pair(chatId, "${MessagesRepository.EXIT_CODE}|"))
            }

            // Close reader channels
            messagesRepository.deleteChat(clientId, chatId)
            messagesRepository.removeTopics(listOf(topicPartnerToClient))
        }

        secretKeyRepository.delete(clientId)
        chatRequestsRepository.delete(clientId)
        clientRepository.delete(clientId)

        return Response.SUCCESS()
    }

    override fun getInitializedClients(): Response<List<Pair<Long, String>>> = Response.SUCCESS(clientRepository.getInitializedClients())

    override fun isInitialized(
        clientId: Long
    ): Response<Boolean> = Response.SUCCESS(clientRepository.isInitialized(clientId))

}