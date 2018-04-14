package com.landg.services.notification.model

import org.springframework.http.HttpStatus

data class ApiError(
    val httpStatus: HttpStatus,
    val message: String? = null,
    val errors: List<String?>
)