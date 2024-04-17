package ru.namerpro.nchatserver.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import ru.namerpro.nchatserver.services.api.ChatManagementService

@RestController
class ChatManagementController @Autowired constructor(
    private val chatManagementService: ChatManagementService
) {

    @PostMapping(value = ["/request_chat/{creator_id}/{partner_id}"])
    fun requestChat(
        @PathVariable(name = "creator_id") creatorId: Long,
        @PathVariable(name = "partner_id") partnerId: Long,
    ): ResponseEntity<HttpStatus> {
        val response = chatManagementService.requestChat(
            creatorId = creatorId,
            partnerId = partnerId
        )
        return if (response.isSuccess) {
            ResponseEntity(HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping(value = ["/ping_chat_requests/{client_id}"])
    fun pingChatRequests(
        @PathVariable(name = "client_id") clientId: Long
    ): ResponseEntity<List<Long>> {
        val response = chatManagementService.pingChatRequest(clientId)
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
    ): ResponseEntity<Long> {
        val response = chatManagementService.createChat(
            creatorId = creatorId,
            partnerId = partnerId
        )
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