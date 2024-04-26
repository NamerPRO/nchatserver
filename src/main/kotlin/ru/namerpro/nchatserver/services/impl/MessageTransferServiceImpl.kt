package ru.namerpro.nchatserver.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import ru.namerpro.nchatserver.model.Response
import ru.namerpro.nchatserver.repositories.ClientRepository
import ru.namerpro.nchatserver.repositories.MessagesRepository
import ru.namerpro.nchatserver.services.api.MessageTransferService
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Paths

@Service
class MessageTransferServiceImpl @Autowired constructor(
    private val clientRepository: ClientRepository,
    private val messagesRepository: MessagesRepository
) : MessageTransferService {

    override fun getMessages(
        clientId: Long,
        chatId: Long
    ): Response<List<String>> {
        val client = clientRepository.retrieve(clientId)
        if (client == null || !client.chatIdToPartner.containsKey(chatId)) {
            return Response.FAILED()
        }
        val messages = messagesRepository.receiveMessages(clientId, chatId)
        return Response.SUCCESS(messages)
    }

    override fun sendMessage(
        clientId: Long,
        chatId: Long,
        message: String
    ): Response<Unit> {
        val client = clientRepository.retrieve(clientId)
        val partnerId = client?.chatIdToPartner?.get(chatId) ?: -1
        if (client == null || !messagesRepository.hasLinkageWith(partnerId, chatId)) {
            return Response.FAILED()
        }
        messagesRepository.sendMessage(clientId, Pair(chatId, "${MessagesRepository.TEXT_MESSAGE_CODE}$message"))
        return Response.SUCCESS()
    }

    override fun uploadFile(
        clientId: Long,
        chatId: Long,
        file: MultipartFile,
        message: String
    ): Response<Unit> {
        try {
            val client = clientRepository.retrieve(clientId) ?: return Response.FAILED()
            val partnerId = client.chatIdToPartner[chatId] ?: return Response.FAILED()
            if (!messagesRepository.hasLinkageWith(partnerId, chatId)) {
                return Response.FAILED()
            }
            val folder = File(FILES_UPLOAD_FOLDER)
            if (!folder.exists()) {
                folder.mkdirs()
            }
            val dest = File("${folder.path}${File.separator}${file.originalFilename}")
            file.transferTo(dest)
            val response = sendMessage(clientId, chatId, message)
            if (response !is Response.SUCCESS) {
                Files.delete(Paths.get(dest.toString()))
                return Response.FAILED()
            }
            return Response.SUCCESS()
        } catch (e: IOException) {
            return Response.FAILED()
        }
    }

    override fun downloadFile(
        fileName: String
    ): Response<Pair<Long, InputStreamResource>> {
        return try {
            val file = File("${FILES_UPLOAD_FOLDER}${File.separator}${fileName}")
            if (file.exists()) {
                Response.SUCCESS(Pair(file.length(), InputStreamResource(file.inputStream())))
            } else {
                Response.FAILED()
            }
        } catch (e: MalformedURLException) {
            Response.FAILED()
        }
    }

    companion object {
        private val FILES_UPLOAD_FOLDER = "${System.getProperty("user.dir")}${File.separator}uploads${File.separator}"
    }

}