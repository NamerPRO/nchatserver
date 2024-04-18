package ru.namerpro.nchatserver.services.api

import ru.namerpro.nchatserver.model.Response

interface InitializationService {

    fun initialize(
        clientName: String
    ): Response<Long>

    fun deinitialize(
        clientId: Long
    ): Response<Unit>

    fun getInitializedClients(): Response<List<Pair<Long, String>>>

    fun isInitialized(
        clientId: Long
    ): Response<Boolean>

}