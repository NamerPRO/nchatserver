package ru.namerpro.nchatserver.repositories

interface ObjectRepository<T, U> {

    fun store(
        id: Long,
        element: T
    )

    fun retrieve(
        id: Long
    ): U

    fun delete(
        id: Long
    )

}