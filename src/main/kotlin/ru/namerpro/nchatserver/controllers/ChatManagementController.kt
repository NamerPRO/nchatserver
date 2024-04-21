package ru.namerpro.nchatserver.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.namerpro.nchatserver.model.Chat
import ru.namerpro.nchatserver.services.api.ChatManagementService

@RestController
class ChatManagementController @Autowired constructor(
    private val chatManagementService: ChatManagementService
) {

    @PostMapping(value = ["/new_chats/{client_id}"])
    fun newChats(
        @PathVariable(name = "client_id") clientId: Long
    ): ResponseEntity<List<Chat>> {
        val response = chatManagementService.newChats(clientId)
        return if (response.isSuccess) {
            ResponseEntity(response.data, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping(value = ["/create_chat/{creator_id}/{partner_id}"])
    fun createChat(
        @PathVariable(name = "creator_id") creatorId: Long,
        @PathVariable(name = "partner_id") partnerId: Long,
        @RequestBody chatData: Triple<String, String, String>
    ): ResponseEntity<Long> {
        val response = chatManagementService.createChat(creatorId, partnerId, chatData.first, chatData.second, chatData.third)
        return if (response.isSuccess) {
            ResponseEntity(response.data, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping(value = ["/is_chat_created/{client_id}/{chat_id}"])
    fun isChatCreated(
        @PathVariable(name = "client_id") clientId: Long,
        @PathVariable(name = "chat_id") chatId: Long
    ): ResponseEntity<Boolean> {
        val response = chatManagementService.isChatCreated(
            clientId = clientId,
            chatId = chatId
        )
        return if (response.isSuccess) {
            ResponseEntity(response.data, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping(value = ["/leave_chat/{client_id}/{chat_id}"])
    fun leaveChat(
        @PathVariable(name = "client_id") clientId: Long,
        @PathVariable(name = "chat_id") chatId: Long
    ): ResponseEntity<HttpStatus> {
        val response = chatManagementService.leaveChat(
            clientId = clientId,
            chatId = chatId
        )
        return if (response.isSuccess) {
            ResponseEntity(HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

}