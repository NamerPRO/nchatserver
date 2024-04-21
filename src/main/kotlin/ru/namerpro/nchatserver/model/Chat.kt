package ru.namerpro.nchatserver.model

data class Chat(
    val chatName: String,
    val chatId: Long,
    val partnerName: String,
    val partnerId: Long,
    val secret: String,
    val cipherType: String
)
