package ru.namerpro.nchatserver.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.namerpro.nchatserver.model.Response
import ru.namerpro.nchatserver.repositories.ClientRepository
import ru.namerpro.nchatserver.repositories.SecretKeyRepository
import ru.namerpro.nchatserver.services.api.SecretKeyExchangeService
import java.math.BigInteger

@Service
class SecretKeyExchangeServiceImpl @Autowired constructor(
    private val clientRepository: ClientRepository,
    private val secretKeyRepository: SecretKeyRepository
) : SecretKeyExchangeService {

    override fun sendPartOfKey(
        senderId: Long,
        receiverId: Long,
        partOfKey: BigInteger
    ): Response<Unit> {
        if (clientRepository.retrieve(senderId) == null
                || clientRepository.retrieve(receiverId) == null) {
            return Response.FAILED()
        }
        secretKeyRepository.store(receiverId, Pair(senderId, partOfKey))
        return Response.SUCCESS()
    }

    override fun getPartsOfKeys(
        clientId: Long
    ): Response<List<Pair<Long, BigInteger>>> {
        if (clientRepository.retrieve(clientId) == null) {
            return Response.FAILED()
        }
        val data = secretKeyRepository.retrieve(clientId)
        secretKeyRepository.delete(clientId)
        return Response.SUCCESS(data)
    }

}