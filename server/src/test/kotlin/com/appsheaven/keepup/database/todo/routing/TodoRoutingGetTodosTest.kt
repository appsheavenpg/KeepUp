package com.appsheaven.keepup.database.todo.routing

import com.appsheaven.keepup.core.ExceptionMapper
import com.appsheaven.keepup.database.todo.domain.TodoFixtures
import com.appsheaven.keepup.todo.infrastructure.TodoResponse
import com.appsheaven.keepup.todos.domain.entities.PagedTodos
import com.appsheaven.keepup.todos.domain.entities.toNetworkTodo
import com.appsheaven.keepup.todos.domain.services.TodoService
import com.appsheaven.keepup.todos.routing.todoRouting
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TodoRoutingGetTodosTest {
    private lateinit var todoService: TodoService
    private lateinit var exceptionMapper: ExceptionMapper

    @BeforeEach
    fun setup() {
        todoService = mockk()
        exceptionMapper = mockk()
    }

    @Test
    fun `getTodos returns 400 when page parameter is missing`() = testApplication {
        // Arrange
        application {
            todoRouting(todoService, exceptionMapper)
        }

        // Act
        val result = createJsonClient().get("/todos?size=10")

        // Assert
        assertEquals(HttpStatusCode.BadRequest, result.status)
        assertEquals("Invalid page", result.bodyAsText())
    }

    @Test
    fun `getTodos returns 400 when size parameter is missing`() = testApplication {
        // Arrange
        application {
            todoRouting(todoService, exceptionMapper)
        }

        // Act
        val result = createJsonClient().get("/todos?page=1")

        // Assert
        assertEquals(HttpStatusCode.BadRequest, result.status)
        assertEquals("Invalid size", result.bodyAsText())
    }

    @Test
    fun `getTodos returns 400 when page parameter is not a number`() = testApplication {
        // Arrange
        application {
            todoRouting(todoService, exceptionMapper)
        }

        // Act
        val result = createJsonClient().get("/todos?page=invalid&size=10")

        // Assert
        assertEquals(HttpStatusCode.BadRequest, result.status)
        assertEquals("Invalid page", result.bodyAsText())
    }

    @Test
    fun `getTodos returns 400 when size parameter is not a number`() = testApplication {
        // Arrange
        application {
            todoRouting(todoService, exceptionMapper)
        }

        // Act
        val result = createJsonClient().get("/todos?page=1&size=invalid").apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            assertEquals("Invalid size", bodyAsText())
        }

        // Assert
        assertEquals(HttpStatusCode.BadRequest, result.status)
        assertEquals("Invalid size", result.bodyAsText())
    }

    @Test
    fun `getTodos returns 400 when page parameter is negative`() = testApplication {
        // Arrange
        application {
            todoRouting(todoService, exceptionMapper)
        }

        // Act
        val result = createJsonClient().get("/todos?page=-1&size=10")

        // Assert
        assertEquals(HttpStatusCode.BadRequest, result.status)
        assertEquals("Page must be greater than 0", result.bodyAsText())
    }

    @Test
    fun `getTodos returns 400 when size parameter is negative`() = testApplication {
        // Arrange
        application {
            todoRouting(todoService, exceptionMapper)
        }

        // Act
        val result = createJsonClient().get("/todos?page=1&size=-10")

        // Assert
        assertEquals(HttpStatusCode.BadRequest, result.status)
        assertEquals("Size must be greater than 0", result.bodyAsText())
    }

    @Test
    fun `getTodos returns 400 when page parameter is zero`() = testApplication {
        // Arrange
        application {
            todoRouting(todoService, exceptionMapper)
        }

        // Act
        val result = createJsonClient().get("/todos?page=0&size=10")

        // Assert
        assertEquals(HttpStatusCode.BadRequest, result.status)
        assertEquals("Page must be greater than 0", result.bodyAsText())
    }

    @Test
    fun `getTodos returns 400 when size parameter is zero`() = testApplication {
        // Arrange
        application {
            todoRouting(todoService, exceptionMapper)
        }

        // Act
        val result = createJsonClient().get("/todos?page=1&size=0")

        // Assert
        assertEquals(HttpStatusCode.BadRequest, result.status)
        assertEquals("Size must be greater than 0", result.bodyAsText())
    }

    @Test
    fun `getTodos returns todos when parameters are valid`() = testApplication {
        // Arrange
        val expected = PagedTodos(TodoFixtures.fiveTodos.map { it.toNetworkTodo() }, true)
        coEvery { todoService.getTodos(any(), any()) } returns expected

        application {
            todoRouting(todoService, exceptionMapper)
        }

        // Act
        val result = createJsonClient().get("/todos?page=1&size=10")
        val responseText = result.bodyAsText()
        val response = Json.decodeFromString<TodoResponse>(responseText)

        // Assert
        assertEquals(HttpStatusCode.OK, result.status)
        assertEquals(expected.items, response.todos)
        assertEquals(expected.hasMore, response.hasMore)
        assertEquals(1, response.currentPage)
        assertEquals(10, response.pageSize)
    }

    @Test
    fun `getTodos handles service exceptions`() = testApplication {
        // Arrange
        val exception = RuntimeException("Service error")
        coEvery { todoService.getTodos(1, 10) } throws exception
        coEvery { exceptionMapper.toErrorResponse(exception) } returns Pair(HttpStatusCode.InternalServerError, "Internal error")

        application {
            todoRouting(todoService, exceptionMapper)
        }

        // Act
        val result = client.get("/todos?page=1&size=10")

        // Assert
        assertEquals(HttpStatusCode.InternalServerError, result.status)
        assertEquals("Internal error", result.bodyAsText())
    }

    private fun ApplicationTestBuilder.createJsonClient(): HttpClient {
        return createClient {
            this@createJsonClient.install(ContentNegotiation) {
                json()
            }
        }
    }
}
