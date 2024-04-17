package ru.namerpro.nchatserver.repositories

import org.springframework.stereotype.Repository
import ru.namerpro.nchatserver.model.Client

@Repository
class ClientRepository : ObjectRepository<Client, Client?> {

    private val clientHolderMap = hashMapOf<Long, Client>()

    override fun store(
        id: Long,
        element: Client
    ) {
        clientHolderMap[id] = element
    }

    override fun retrieve(
        id: Long
    ): Client? {
        return clientHolderMap[id]
    }

    override fun delete(
        id: Long
    ) {
        if (clientHolderMap.containsKey(id)) {
            clientHolderMap.remove(id)
        }
    }

    fun getInitializedClients(): List<Pair<Long, String>> {
        return clientHolderMap.map {
            Pair(it.key, it.value.name)
        }.toList()
    }

}