package ru.namerpro.nchatserver.repositories

import org.springframework.stereotype.Repository

@Repository
class NewChatsRepository : ObjectRepository<Triple<Pair<Long, String>, Pair<Long, String>, String>, List<Triple<Pair<Long, String>, Pair<Long, String>, String>>> {

    // client id -> { chat id + chat name } + { partner id + partner name } + secret
    private val newChatsHolder = HashMap<Long, MutableList<Triple<Pair<Long, String>, Pair<Long, String>, String>>>()

    override fun store(
        id: Long,
        element: Triple<Pair<Long, String>, Pair<Long, String>, String>
    ) {
        val data = newChatsHolder.getOrPut(id) { mutableListOf() }
        data.add(element)
    }

    override fun retrieve(
        id: Long
    ): List<Triple<Pair<Long, String>, Pair<Long, String>, String>> = newChatsHolder[id] ?: emptyList()

    override fun delete(
        id: Long
    ) {
        if (newChatsHolder.containsKey(id)) {
            newChatsHolder.remove(id)
        }
    }

}