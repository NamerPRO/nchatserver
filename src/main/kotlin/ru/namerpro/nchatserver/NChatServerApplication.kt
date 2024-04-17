package ru.namerpro.nchatserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NChatServerApplication

fun main(
	args: Array<String>
) {
	runApplication<NChatServerApplication>(*args)
}
