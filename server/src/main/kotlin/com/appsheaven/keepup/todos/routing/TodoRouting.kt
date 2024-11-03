package com.appsheaven.keepup.todos.routing

import com.appsheaven.keepup.core.ExceptionMapper
import com.appsheaven.keepup.todo.infrastructure.TodoRequest
import com.appsheaven.keepup.todo.infrastructure.TodoResponse
import com.appsheaven.keepup.todos.domain.services.TodoService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

private const val TODO_PATH = "/todos"
private const val PAGE_PARAM = "page"
private const val SIZE_PARAM = "size"

fun Application.todoRouting(todoService: TodoService, exceptionMapper: ExceptionMapper) {
    routing {
        get(TODO_PATH) {
            val page = requireNotNull(call.parameters[PAGE_PARAM]?.toIntOrNull()) {
                call.respond(HttpStatusCode.BadRequest, "Invalid page")
                return@get
            }

            require(page > 0) {
                call.respond(HttpStatusCode.BadRequest, "Page must be greater than 0")
                return@get
            }

            val size = requireNotNull(call.parameters[SIZE_PARAM]?.toIntOrNull()) {
                call.respond(HttpStatusCode.BadRequest, "Invalid size")
                return@get
            }

            require(size > 0) {
                call.respond(HttpStatusCode.BadRequest, "Size must be greater than 0")
                return@get
            }

            runCatching { todoService.getTodos(page, size) }
                .onSuccess { todos ->
                    val response = TodoResponse(
                        todos = todos.items,
                        currentPage = page,
                        pageSize = size,
                        hasMore = todos.hasMore
                    )
                    call.respond(HttpStatusCode.OK, response)
                }
                .onFailure { error ->
                    val (status, message) = exceptionMapper.toErrorResponse(error)
                    call.respond(status, message)
                    return@get
                }
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
