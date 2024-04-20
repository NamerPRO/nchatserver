package ru.namerpro.nchatserver.repositories

import org.springframework.stereotype.Repository

@Repository
class SecretKeyRepository : ObjectRepository<Pair<Long, String>, List<Pair<Long, String>>> {

    // client id -> pairs { chatId, shareable }
    private val partsOfKeysHolder = HashMap<Long, MutableList<Pair<Long, String>>>()

    override fun store(
        id: Long,
        element: Pair<Long, String>
    ) {
        val data = partsOfKeysHolder.getOrPut(id) { mutableListOf() }
        data.add(element)
    }

    override fun retrieve(
        id: Long
    ): List<Pair<Long, String>> = partsOfKeysHolder[id] ?: emptyList()

    override fun delete(
        id: Long
    ) {
        if (partsOfKeysHolder.containsKey(id)) {
            partsOfKeysHolder.remove(id)
        }
    }

}