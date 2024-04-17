package ru.namerpro.nchatserver.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.namerpro.nchatserver.services.api.SecretKeyExchangeService
import java.math.BigInteger

@RestController
class SecretKeyExchangeController @Autowired constructor(
    private val secretKeyExchangeService: SecretKeyExchangeService
) {

    @PostMapping(value = ["/send_part_of_key/{sender_id}/{receiver_id}"])
    fun sendPartOfKey(
        @PathVariable(name = "sender_id") senderId: Long,
        @PathVariable(name = "receiver_id") receiverId: Long,
        @RequestBody key: String
    ): ResponseEntity<HttpStatus> {
        val response = secretKeyExchangeService.sendPartOfKey(
            senderId = senderId,
            receiverId = receiverId,
            key.toBigInteger()
        )
        return if (response.isSuccess) {
            ResponseEntity(HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping(value = ["/get_parts_of_keys/{client_id}"])
    fun getPartsOfKeys(
        @PathVariable(name = "client_id") clientId: Long
    ): ResponseEntity<List<Pair<Long, BigInteger>>> {
        val response = secretKeyExchangeService.getPartsOfKeys(clientId)
        return if (response.isSuccess) {
            ResponseEntity(response.data, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

}