import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.landg.services"
version = "0.0.1-SNAPSHOT"
description = "Web Push Notification Service"

plugins {
    val kotlinVersion = "1.2.31"
    val springVersion = "2.0.1.RELEASE"
    val springIOVersion = "1.0.5.RELEASE"

    id("org.springframework.boot") version springVersion
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
    id("io.spring.dependency-management") version springIOVersion
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8" }
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}

repositories {
    mavenLocal()
    jcenter()
    mavenCentral()
}

dependencies {
    compile("org.springframework.boot:spring-boot-starter-web")
    compile("org.springframework.boot:spring-boot-starter-data-jpa")
    compile("com.h2database:h2")
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compile("org.jetbrains.kotlin:kotlin-reflect")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin")
    compile("io.springfox:springfox-swagger-ui:2.0.1")
    compile("io.springfox:springfox-swagger2:2.0.1")
    compile("org.bouncycastle:bcprov-jdk15on:1.59")
    compile("nl.martijndwars:web-push:3.1.0")
    testCompile("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}