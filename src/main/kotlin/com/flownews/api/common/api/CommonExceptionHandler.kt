package com.flownews.api.common.api

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException

@RestControllerAdvice
class CommonExceptionHandler {
    private val logger = LoggerFactory.getLogger(CommonExceptionHandler::class.java)

    @ExceptionHandler(NoHandlerFoundException::class)
    fun noHandlerFoundException(ex: NoHandlerFoundException): ResponseEntity<String> {
        logger.warn(ex.message)

        return ResponseEntity.notFound().build()
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<String> {
        logger.error(ex.message, ex)

        return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다: ${ex.message}")
    }
}
