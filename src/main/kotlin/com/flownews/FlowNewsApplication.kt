package com.flownews

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FlowNewsApplication

fun main(args: Array<String>) {
	runApplication<FlowNewsApplication>(*args)
}
