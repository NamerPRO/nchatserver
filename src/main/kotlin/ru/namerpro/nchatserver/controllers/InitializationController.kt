package ru.namerpro.nchatserver.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import ru.namerpro.nchatserver.services.api.InitializationService

@RestController
class InitializationController @Autowired constructor(
    private val initializationService: InitializationService
) {

    @PostMapping(value = ["/initialize/{client_name}"])
    fun initialize(
        @PathVariable(name = "client_name") clientName: String
    ): ResponseEntity<Long> {
        val response = initializationService.initialize(clientName)
        return if (response.isSuccess) {
            ResponseEntity(response.data, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping(value = ["/deinitialize/{client_id}"])
    fun deinitialize(
        @PathVariable(name = "client_id") clientId: Long
    ): ResponseEntity<HttpStatus> {
        val response = initializationService.deinitialize(clientId)
        return if (response.isSuccess) {
            ResponseEntity(HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping(value = ["/get_initialized_clients"])
    fun getInitializedClients(): ResponseEntity<List<Pair<Long, String>>> {
        val response = initializationService.getInitializedClients()
        return if (response.isSuccess) {
            ResponseEntity(response.data, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

}