# Notification Service

This is a Spring Boot service that allows a [web app client](http://udcbitbucket.lgnet.co.uk/projects/CS/repos/web-push-notification-client/browse) to register itself as a subscriber to web push notifications. The service can also trigger web push notifications using the subscriber details.


## IDE

This application is written in Kotlin, the build process below should download the necessary Kotlin dependencies to build your project, but you may need to configure your IDE with Kotlin support before you can run the project in your IDE.
 
The recommended IDE for this project is IntelliJ because Jetbrains are the authors of the Kotlin language and there is a wealth of support online.

[Download IntelliJ](https://www.jetbrains.com/idea/download/)

## Building and Running

Alternatively, you can use the build process below.

Environment Setup: ``$ ./gradlew`` or ``$ ./gradlew.bat``

Build: ``$ gradle build``

Run: ``gradle bootRun``

Test: ``gradle test``

## Gradle Reports

Tests: ``./build/reports/tests/test/index.html``

## Swagger

Swagger2 documentation is produced at runtime.

With the service running, and not in production mode, you can view the Swagger documentation at: ``$SERVICE_URL/swagger-ui.html``

For example:
``http://localhost:9001/notifications/swagger-ui.html``
