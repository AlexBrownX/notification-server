package com.landg.services.notification.repository

import com.landg.services.notification.model.PushSubscriber
import org.springframework.data.repository.CrudRepository

interface SubscriberRepository: CrudRepository<PushSubscriber, Long> {

    fun findByEndpoint(endpoint: String): List<PushSubscriber>
}