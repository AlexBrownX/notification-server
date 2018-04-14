package com.landg.services.notification.model

import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Subscriber(
    @GeneratedValue @Id val clientIdentifier: Long? = 0,
    val endpoint: String? = "",
    val expirationTime: String? = "",
    @ElementCollection val keys: Map<String, String>? = HashMap()
)
