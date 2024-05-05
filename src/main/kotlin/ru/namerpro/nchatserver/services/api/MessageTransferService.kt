package ru.namerpro.nchatserver.services.api

import org.springframework.core.io.InputStreamResource
import org.springframework.web.multipart.MultipartFile
import ru.namerpro.nchatserver.model.Response

interface MessageTransferService {

    fun getMessages(
        clientId: Long,
        chatId: Long
    ): Response<List<String>>

    fun sendMessage(
        clientId: Long,
        chatId: Long,
        message: String
    ): Response<Unit>

    fun uploadFile(
        fullFileLength: Long,
        clientId: Long,
        chatId: Long,
        file: MultipartFile,
        message: String
    ): Response<Unit>

    fun downloadFile(
        fileName: String
    ): Response<Pair<Long, InputStreamResource>>

}