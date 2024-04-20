package ru.namerpro.nchatserver.repositories

import org.springframework.stereotype.Repository
import java.util.HashMap

@Repository
class NewChatsRepository : ObjectRepository<Triple<Long, Long, String>, List<Triple<Long, Long, String>>> {

    // client id -> chat id + partner id
    private val newChatsHolder = HashMap<Long, MutableList<Triple<Long, Long, String>>>()

    override fun store(
        id: Long,
        element: Triple<Long, Long, String>
    ) {
        val data = newChatsHolder.getOrPut(id) { mutableListOf() }
        data.add(element)
    }

    override fun retrieve(
        id: Long
    ): List<Triple<Long, Long, String>> = newChatsHolder[id] ?: emptyList()

    override fun delete(
        id: Long
    ) {
        if (newChatsHolder.containsKey(id)) {
            newChatsHolder.remove(id)
        }
    }

}