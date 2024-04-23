package ru.namerpro.nchatserver.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.namerpro.nchatserver.services.api.MessageTransferService

@RestController
class MessageTransferController @Autowired constructor(
    private val messageTransferService: MessageTransferService
) {

    @PostMapping(value = ["/get_messages/{client_id}/{chat_id}"])
    fun getMessages(
        @PathVariable(name = "client_id") clientId: Long,
        @PathVariable(name = "chat_id") chatId: Long
    ): ResponseEntity<List<String>> {
        val response = messageTransferService.getMessages(clientId, chatId)
        return if (response.isSuccess) {
            ResponseEntity(response.data, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping(value = ["/send_message/{client_id}/{chat_id}"])
    fun sendMessage(
        @PathVariable(name = "client_id") clientId: Long,
        @PathVariable(name = "chat_id") chatId: Long,
        @RequestBody message: String
    ): ResponseEntity<HttpStatus> {
        val response = messageTransferService.sendMessage(clientId, chatId, message)
        return if (response.isSuccess) {
            ResponseEntity(HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

}