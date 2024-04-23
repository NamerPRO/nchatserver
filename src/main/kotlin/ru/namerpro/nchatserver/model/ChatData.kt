package ru.namerpro.nchatserver.model

data class ChatData(
    val chatName: String,
    val cipherType: String,
    val secret: String,
    val iv: String
)
