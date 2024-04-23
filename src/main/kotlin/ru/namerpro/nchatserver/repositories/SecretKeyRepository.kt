package ru.namerpro.nchatserver.repositories

import org.springframework.stereotype.Repository
import ru.namerpro.nchatserver.model.Secret

@Repository
class SecretKeyRepository : ObjectRepository<Secret, List<Secret>> {

    private val partsOfKeysHolder = HashMap<Long, MutableList<Secret>>()

    override fun store(
        id: Long,
        element: Secret
    ) {
        val data = partsOfKeysHolder.getOrPut(id) { mutableListOf() }
        data.add(element)
    }

    override fun retrieve(
        id: Long
    ): List<Secret> = partsOfKeysHolder[id] ?: emptyList()

    override fun delete(
        id: Long
    ) {
        if (partsOfKeysHolder.containsKey(id)) {
            partsOfKeysHolder.remove(id)
        }
    }

}