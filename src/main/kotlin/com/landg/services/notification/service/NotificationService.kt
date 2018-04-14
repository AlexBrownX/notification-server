package com.landg.services.notification.service

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.landg.services.notification.exception.ServiceException
import com.landg.services.notification.model.NotificationRequest
import com.landg.services.notification.model.Subscriber
import com.landg.services.notification.repository.SubscriberRepository
import nl.martijndwars.webpush.Notification
import nl.martijndwars.webpush.PushService
import nl.martijndwars.webpush.Subscription
import org.apache.http.HttpResponse
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.Security

@Service
class NotificationService(val subscriberRepository: SubscriberRepository) {

    companion object {
        private val LOG = LoggerFactory.getLogger(NotificationService::class.java)
    }

    @Value("\${vapid.publicKey}")
    val publicKey: String? = null

    @Value("\${vapid.privateKey}")
    val privateKey: String? = null

    @Value("\${vapid.subject}")
    val subject: String? = null

    fun storeSubscriber(subscriber: Subscriber): Long {
        val result = this.subscriberRepository.save(subscriber)
        LOG.info("Storing subscriber object: $result")
        return result.clientIdentifier?: throw ServiceException("Could not generate client identifier to store subscriber")
    }

    fun sendNotification(notificationRequest: NotificationRequest): HttpResponse {
        Security.addProvider(BouncyCastleProvider())

        val subscriber = fetchSubscriber(notificationRequest);
        val pushService = PushService(publicKey, privateKey, subject)
        val subscription = createSubscription(subscriber)
        val payload = createPayload(notificationRequest)
        val notification = Notification(subscription, payload)
        LOG.info("Sending payload: $payload")

        val httpResponse = pushService.send(notification)
        LOG.info("Push Service Response: $httpResponse")

        return httpResponse
    }

    fun fetchSubscriber(notificationRequest: NotificationRequest): Subscriber {
        val subscriber = this.subscriberRepository.findById(notificationRequest.clientIdentifier)
        LOG.info("Retrieved subscription object: $subscriber")

        return subscriber.get()
    }

    fun createSubscription(subscriber: Subscriber): Subscription {
        val jsonSub = Gson().toJson(subscriber);
        return Gson().fromJson(jsonSub, Subscription::class.java)
    }

    /*
        TODO - These are valid Chrome notification properties

        "actions"
        "body"
        "dir"
        "icon"
        "lang"
        "renotify"
        "requireInteraction"
        "tag"
        "vibrate"
        "data"
     */

    fun createPayload(notificationRequest: NotificationRequest): String {
        val notificationDetails = JsonObject()
        notificationDetails.addProperty("title", notificationRequest.title)
        notificationDetails.addProperty("body", notificationRequest.body)
        notificationDetails.addProperty("data", notificationRequest.data)

        val jsonPayload = JsonObject()
        jsonPayload.add("notification", notificationDetails)

        return jsonPayload.toString()
    }
}
