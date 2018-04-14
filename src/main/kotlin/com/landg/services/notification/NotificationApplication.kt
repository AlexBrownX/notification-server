package com.landg.services.notification

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@Configuration
class NotificationApplication

fun main(args: Array<String>) {
    runApplication<NotificationApplication>(*args)
}