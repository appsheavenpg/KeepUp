plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "com.appsheaven.keepup"
version = "1.0.0"
application {
    mainClass.set("com.appsheaven.keepup.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.mongodb.driver.kotlin.coroutines)
    implementation(libs.mongodb.bson.kotlinx)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.kotlin.test.mockk)
    testImplementation(libs.kotlin.test.coroutines)
    testImplementation(libs.test.containers)
    testImplementation(libs.apache.commons.compress)
    testImplementation(libs.test.containers.mongodb)
    testImplementation(libs.test.containers.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}
