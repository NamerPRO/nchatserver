package ru.namerpro.nchatserver.model

data class Client(
    val name: String,
    val chatIdToPartner: HashMap<Long, Long> = hashMapOf()
)