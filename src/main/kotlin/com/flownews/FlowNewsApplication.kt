package com.flownews

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class FlowNewsApplication

fun main(args: Array<String>) {
    runApplication<FlowNewsApplication>(*args)
}
