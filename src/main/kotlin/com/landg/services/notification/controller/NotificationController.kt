package com.landg.services.notification.controller

import com.google.gson.Gson
import com.landg.services.notification.model.NotificationRequest
import com.landg.services.notification.model.PushSubscriber
import com.landg.services.notification.service.NotificationService
import com.wordnik.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
class NotificationController(val notificationService: NotificationService) {

    companion object {
        private val LOG = LoggerFactory.getLogger(NotificationController::class.java)
    }

    @PostMapping("/registerSubscriber")
    @ApiOperation(
        value = "Stores a pushSubscriber",
        notes = "Responds with unique client ID if the subscriber was stored successfully"
    )
    fun subscribe(@RequestBody pushSubscriber: PushSubscriber): ResponseEntity<Long> {
        LOG.info("PushSubscriber received: $pushSubscriber")

        val id = this.notificationService.storeSubscriber(pushSubscriber)
        return ResponseEntity.ok(id)
}

    @PostMapping("/sendNotification")
    @ApiOperation(
            value = "Trigger a notification push containing the provided notificationRequest",
            notes = "Responds with OK if the notification push was successful"
    )
    fun sendNotification(@RequestBody notificationRequest: NotificationRequest): ResponseEntity<Any> {
        LOG.info("NotificationRequest received: $notificationRequest")

        val pushServiceHttpResponse = notificationService.sendNotification(notificationRequest)
        return ResponseEntity.ok(Gson().toJson(pushServiceHttpResponse))
    }
}