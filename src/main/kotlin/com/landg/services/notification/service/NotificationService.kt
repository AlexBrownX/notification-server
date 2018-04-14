package com.landg.services.notification.service

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.landg.services.notification.exception.ServiceException
import com.landg.services.notification.model.NotificationRequest
import com.landg.services.notification.model.PushSubscriber
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

    fun storeSubscriber(pushSubscriber: PushSubscriber): Long? {
        val existingSubscription = this.subscriberRepository.findByEndpoint(pushSubscriber.endpoint.orEmpty())

        if (!existingSubscription.isEmpty()) {
            LOG.info("Push subscriber already exists, sending back existing client identifier ${existingSubscription[0].clientIdentifier}")
            return existingSubscription[0].clientIdentifier
        }

        val result = this.subscriberRepository.save(pushSubscriber)

        LOG.info("Storing pushSubscriber object: $result")
        return result.clientIdentifier?: throw ServiceException("Could not generate client identifier to store pushSubscriber")
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

    fun fetchSubscriber(notificationRequest: NotificationRequest): PushSubscriber {
        val subscriber = this.subscriberRepository.findById(notificationRequest.clientIdentifier)
        LOG.info("Retrieved subscription object: $subscriber")

        return subscriber.get()
    }

    fun createSubscription(pushSubscriber: PushSubscriber): Subscription {
        val jsonSub = Gson().toJson(pushSubscriber);
        return Gson().fromJson(jsonSub, Subscription::class.java)
    }

    fun createPayload(notificationRequest: NotificationRequest): String {
        val notificationDetails = JsonObject()
        notificationDetails.addProperty("title", notificationRequest.title)
        notificationDetails.addProperty("body", notificationRequest.body)
        notificationDetails.addProperty("icon", "https://www.shareicon.net/data/128x128/2015/12/24/692291_business_512x512.png")
        notificationDetails.addProperty("badge", "https://www.shareicon.net/data/128x128/2015/12/24/692291_business_512x512.png")
        notificationDetails.addProperty("lang", "en")
        notificationDetails.addProperty("renotify", false)
        notificationDetails.addProperty("requireInteraction", "true")

//        val notificationActions = JsonObject()
//        notificationActions.addProperty("action", "open")
//        notificationActions.addProperty("title", "Open")
//
//        val notificationActionsArray = JsonArray()
//        notificationActionsArray.add(notificationActions)
//
//        notificationDetails.add("actions", notificationActionsArray)

        val notificationData = JsonObject()
        notificationData.addProperty("url", "http://localhost:4200/")
        notificationData.addProperty("numberOfCases", 7)
        notificationData.addProperty("readyCases", 5)
        notificationData.addProperty("requireActionCases", 1)
        notificationData.addProperty("inProgressCases", 1)
        notificationData.addProperty("noActionCases", 0)

        notificationDetails.add("data", notificationData)

        val jsonPayload = JsonObject()
        jsonPayload.add("notification", notificationDetails)

        val result = jsonPayload.toString();

        LOG.info("JSON Payload: $result")
        return result
    }
}
