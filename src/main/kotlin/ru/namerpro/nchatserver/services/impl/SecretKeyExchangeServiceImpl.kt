package ru.namerpro.nchatserver.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.namerpro.nchatserver.model.Response
import ru.namerpro.nchatserver.model.Secret
import ru.namerpro.nchatserver.repositories.ClientRepository
import ru.namerpro.nchatserver.repositories.MessagesRepository
import ru.namerpro.nchatserver.repositories.SecretKeyRepository
import ru.namerpro.nchatserver.services.api.SecretKeyExchangeService

@Service
class SecretKeyExchangeServiceImpl @Autowired constructor(
    private val clientRepository: ClientRepository,
    private val messagesRepository: MessagesRepository,
    private val secretKeyRepository: SecretKeyRepository
) : SecretKeyExchangeService {

    override fun sendPartOfKey(
        receiverId: Long,
        chatId: Long,
        partOfKey: String
    ): Response<Unit> {
        if (clientRepository.retrieve(receiverId) == null
                || !messagesRepository.hasLinkageWith(receiverId, chatId)) {
            return Response.FAILED()
        }
        secretKeyRepository.store(receiverId, Secret(chatId, partOfKey))
        return Response.SUCCESS()
    }

    override fun getPartsOfKeys(
        clientId: Long
    ): Response<List<Secret>> {
        if (clientRepository.retrieve(clientId) == null) {
            return Response.FAILED()
        }
        val data = secretKeyRepository.retrieve(clientId)
        secretKeyRepository.delete(clientId)
        return Response.SUCCESS(data)
    }

}