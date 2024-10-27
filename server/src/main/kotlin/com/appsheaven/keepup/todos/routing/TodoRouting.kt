package com.appsheaven.keepup.todos.routing

import com.appsheaven.keepup.Greeting
import com.appsheaven.keepup.core.ExceptionMapper
import com.appsheaven.keepup.todo.infrastructure.TodoRequest
import com.appsheaven.keepup.todos.domain.services.TodoService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.todoRouting(todoService: TodoService, exceptionMapper: ExceptionMapper) {
    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
        post("/") {
            val result = runCatching { call.receive<TodoRequest>() }
            result.exceptionOrNull()?.let {
                val (status, message) = exceptionMapper.toErrorResponse(it)
                call.respond(status, message)
                return@post
            }

            val todoToInsert = result.getOrThrow().item

            val insertResult = runCatching { todoService.insert(todoToInsert) }
            insertResult.exceptionOrNull()?.let {
                val (status, message) = exceptionMapper.toErrorResponse(it)
                call.respond(status, message)
                return@post
            }

            call.respond(HttpStatusCode.Created)
        }
    }
}
