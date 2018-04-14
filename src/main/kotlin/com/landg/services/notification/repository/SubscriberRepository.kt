package com.landg.services.notification.repository

import com.landg.services.notification.model.Subscriber
import org.springframework.data.repository.CrudRepository

interface SubscriberRepository: CrudRepository<Subscriber, Long> {

    fun findByClientIdentifier(clientIdentifier: Long): List<Subscriber>
}