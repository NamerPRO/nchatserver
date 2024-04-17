package ru.namerpro.nchatserver.repositories

import org.springframework.stereotype.Repository
import java.util.HashMap

@Repository
class ChatRequestsRepository : ObjectRepository<Long, List<Long>> {

    // client id -> partner ids
    private val chatRequestsHolder = HashMap<Long, MutableList<Long>>()

    override fun store(
        id: Long,
        element: Long
    ) {
        val data = chatRequestsHolder.getOrPut(element) { mutableListOf() }
        data.add(id)
    }

    override fun retrieve(
        id: Long
    ): List<Long> = chatRequestsHolder[id] ?: emptyList()

    override fun delete(
        id: Long
    ) {
        if (chatRequestsHolder.containsKey(id)) {
            chatRequestsHolder.remove(id)
        }
    }

}