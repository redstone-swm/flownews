package com.flownews.api.common.api

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException

@RestControllerAdvice
class CommonExceptionHandler {
    private val logger = LoggerFactory.getLogger(CommonExceptionHandler::class.java)

    @ExceptionHandler(NoHandlerFoundException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun noHandlerFoundException(ex: NoHandlerFoundException): ResponseEntity<String> {
        logger.warn(ex.message, ex)

        return ResponseEntity.badRequest().body(ex.message)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(ex: Exception): ResponseEntity<String> {
        logger.error(ex.message, ex)

        return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다: ${ex.message}")
    }
}
