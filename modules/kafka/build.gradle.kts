plugins {
    `java-test-fixtures`

    val kotlinVersion = "2.0.20"

    id("org.jetbrains.kotlin.jvm") version(kotlinVersion)
    id("org.jetbrains.kotlin.plugin.spring") version(kotlinVersion)
}

dependencies {
    api("org.springframework.kafka:spring-kafka")

    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.testcontainers:kafka")

    testFixturesImplementation("org.testcontainers:kafka")
}
