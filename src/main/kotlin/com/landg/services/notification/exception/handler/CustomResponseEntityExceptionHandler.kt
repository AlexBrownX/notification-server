package com.landg.services.notification.exception.handler

import com.landg.services.notification.model.ApiError
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class CustomResponseEntityExceptionHandler {

    companion object {
        private val LOG = LoggerFactory.getLogger(CustomResponseEntityExceptionHandler::class.java)
    }

    @ExceptionHandler(Exception::class)
    protected fun handleException(exception: Exception): ResponseEntity<Any> {
        LOG.error(exception.javaClass.name, exception)
        val httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        val apiError = ApiError(httpStatus, "Server error", listOf(exception.localizedMessage))

        return ResponseEntity(apiError, HttpHeaders(), apiError.httpStatus)
    }
}