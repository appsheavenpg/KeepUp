package com.appsheaven.keepup.database.todo.routing

import com.appsheaven.keepup.core.ExceptionMapper
import com.appsheaven.keepup.database.todo.infrastructure.NetworkTodoFixtures
import com.appsheaven.keepup.todos.domain.services.TodoService
import com.appsheaven.keepup.todos.routing.todoRouting
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TodoRoutingPostTodoTest {

    private lateinit var mockedTodoService: TodoService
    private lateinit var mockedExceptionMapper: ExceptionMapper

    @BeforeTest
    fun before() {
        mockedTodoService = mockk()
        mockedExceptionMapper = mockk()
    }

    @Test
    fun `should post todo`() = testApplication {
        // Arrange
        application { todoRouting(mockedTodoService, mockedExceptionMapper) }

        coEvery { mockedTodoService.insert(any()) } returns Unit

        val client = createJsonClient()

        // Act
        val response = client.postTodoRequest()

        // Assert
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals("", response.bodyAsText())
    }

    @Test
    fun `returns error on invalid request`() = testApplication {
        // Arrange
        application { todoRouting(mockedTodoService, mockedExceptionMapper) }

        coEvery { mockedExceptionMapper.toErrorResponse(any()) } returns (HttpStatusCode.BadRequest to "error")

        val client = createJsonClient()

        // Act
        val response = client.postTodoRequest()

        // Assert
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals("error", response.bodyAsText())
    }

    @Test
    fun `returns error on invalid insert`() = testApplication {
        // Arrange
        application { todoRouting(mockedTodoService, mockedExceptionMapper) }

        val insertException = Exception("Insert error")
        coEvery { mockedTodoService.insert(any()) } throws insertException
        coEvery { mockedExceptionMapper.toErrorResponse(insertException) } returns (HttpStatusCode.BadRequest to "error")

        val client = createJsonClient()

        // Act
        val response = client.postTodoRequest()

        // Assert
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals("error", response.bodyAsText())
    }

    private fun ApplicationTestBuilder.createJsonClient(): HttpClient {
        return createClient {
            this@createJsonClient.install(ContentNegotiation) {
                json()
            }
        }
    }

    private suspend fun HttpClient.postTodoRequest(): HttpResponse {
        return post("/") {
            contentType(ContentType.Application.Json)
            setBody(NetworkTodoFixtures.anyNetworkTodoRequestJson)
        }
    }
}
