package com.landg.services.notification.model

data class NotificationRequest(
    val clientIdentifier: Long,
    val title: String,
    val body: String
)