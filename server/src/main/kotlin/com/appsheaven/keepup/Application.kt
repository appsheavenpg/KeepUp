package com.appsheaven.keepup

import com.appsheaven.keepup.core.ExceptionMapper
import com.appsheaven.keepup.todos.di.TodoExceptionMapper
import com.appsheaven.keepup.todos.di.todoModule
import com.appsheaven.keepup.todos.domain.services.TodoService
import com.appsheaven.keepup.todos.routing.todoRouting
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.yaml references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    installKoin(environment.config.mongodbKey())
    installSerialization()

    val todoService: TodoService by inject()
    val todoExceptionMapper: ExceptionMapper by inject(named(TodoExceptionMapper))

    todoRouting(todoService, todoExceptionMapper)
}

private fun ApplicationConfig.mongodbKey(): String = this.property("ktor.environment.mongodbKey").getString()

private fun Application.installKoin(mongodbKey: String) {
    install(Koin) {
        modules(todoModule(mongodbKey))
    }
}

private fun Application.installSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
}
