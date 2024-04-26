package ru.namerpro.nchatserver.controllers

import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.namerpro.nchatserver.services.api.MessageTransferService

@RestController
class MessageTransferController @Autowired constructor(
    private val response: HttpServletResponse,
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

    @PostMapping("/upload_file/{client_id}/{chat_id}")
    fun uploadFIle(
        @PathVariable(name = "client_id") clientId: Long,
        @PathVariable(name = "chat_id") chatId: Long,
        @RequestParam("file") file: MultipartFile,
        @RequestParam("message") message: String,
    ): ResponseEntity<HttpStatus> {
        val response = messageTransferService.uploadFile(clientId, chatId, file, message)
        return if (response.isSuccess) {
            ResponseEntity(HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/download_file/{file_name}")
    fun downloadFiles(
        @PathVariable(name = "file_name") fileName: String
    ): ResponseEntity<InputStreamResource> {
        val response = messageTransferService.downloadFile(fileName)
        return if (response.isSuccess) {
            ResponseEntity.ok()
                .contentLength(response.data!!.first)
                .body(response.data.second)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

}