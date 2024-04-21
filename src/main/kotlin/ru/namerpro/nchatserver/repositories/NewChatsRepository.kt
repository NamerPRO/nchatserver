package ru.namerpro.nchatserver.repositories

import org.springframework.stereotype.Repository
import ru.namerpro.nchatserver.model.Chat

@Repository
class NewChatsRepository : ObjectRepository<Chat, List<Chat>> {

    // client id -> { chat id + chat name } + { partner id + partner name } + secret + cipher type
    private val newChatsHolder = HashMap<Long, MutableList<Chat>>()

    override fun store(
        id: Long,
        element: Chat
    ) {
        val data = newChatsHolder.getOrPut(id) { mutableListOf() }
        data.add(element)
    }

    override fun retrieve(
        id: Long
    ): List<Chat> = newChatsHolder[id] ?: emptyList()

    override fun delete(
        id: Long
    ) {
        if (newChatsHolder.containsKey(id)) {
            newChatsHolder.remove(id)
        }
    }

}