package ru.namerpro.nchatserver.repositories

import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.util.HashMap

@Repository
class SecretKeyRepository : ObjectRepository<Pair<Long, BigInteger>, List<Pair<Long, BigInteger>>> {

    // client id -> pairs { partnerId, shareable }
    private val partsOfKeysHolder = HashMap<Long, MutableList<Pair<Long, BigInteger>>>()

    override fun store(
        id: Long,
        element: Pair<Long, BigInteger>
    ) {
        val data = partsOfKeysHolder.getOrPut(id) { mutableListOf() }
        data.add(element)
    }

    override fun retrieve(
        id: Long
    ): List<Pair<Long, BigInteger>> = partsOfKeysHolder[id] ?: emptyList()

    override fun delete(
        id: Long
    ) {
        if (partsOfKeysHolder.containsKey(id)) {
            partsOfKeysHolder.remove(id)
        }
    }

}