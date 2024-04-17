package ru.namerpro.nchatserver.model

sealed class Response<T>(
    val data: T? = null,
    val message: String? = null,
    val isSuccess: Boolean
) {

    class SUCCESS<T>(
        data: T? = null
    ): Response<T>(data, null, true)

    class FAILED<T>(
        message: String? = null
    ) : Response<T>(null, message, false)

}