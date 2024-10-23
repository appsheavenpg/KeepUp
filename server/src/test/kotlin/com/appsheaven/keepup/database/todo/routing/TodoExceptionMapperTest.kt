package com.appsheaven.keepup.database.todo.routing

import com.appsheaven.keepup.todos.routing.TodoExceptionMapper
import com.mongodb.ErrorCategory
import com.mongodb.MongoException
import com.mongodb.MongoWriteException
import com.mongodb.WriteError
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.ContentTransformationException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.serialization.SerializationException
import java.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals

class TodoExceptionMapperTest {

    private val todoExceptionMapper = TodoExceptionMapper()

    @Test
    fun `returns BadRequest when ContentTransformationException is thrown`() {
        // Arrange
        val exception = mockk<ContentTransformationException>(relaxed = true)

        // Act
        val (status, message) = todoExceptionMapper.toErrorResponse(exception)

        // Assert
        assertEquals(status, HttpStatusCode.BadRequest)
        assert(message == "Invalid request data")
    }

    @Test
    fun `returns BadRequest when SerializationException is thrown`() {
        // Arrange
        val exception = mockk<SerializationException>(relaxed = true)

        // Act
        val (status, message) = todoExceptionMapper.toErrorResponse(exception)

        // Assert
        assertEquals(status, HttpStatusCode.BadRequest)
        assert(message == "Serialization error")
    }

    @Test
    fun `returns BadRequest when BadRequestException is thrown`() {
        // Arrange
        val exception = mockk<BadRequestException>(relaxed = true)

        // Act
        val (status, message) = todoExceptionMapper.toErrorResponse(exception)

        // Assert
        assertEquals(status, HttpStatusCode.BadRequest)
        assert(message == "Bad request")
    }

    @Test
    fun `returns InternalServerError when IOException is thrown`() {
        // Arrange
        val exception = mockk<IOException>(relaxed = true)

        // Act
        val (status, message) = todoExceptionMapper.toErrorResponse(exception)

        // Assert
        assertEquals(status, HttpStatusCode.InternalServerError)
        assert(message == "I/O error")
    }

    @Test
    fun `returns Conflict when MongoWriteException is thrown with DUPLICATE_KEY`() {
        // Arrange
        val exception = mockk<MongoWriteException>(relaxed = true)
        val writerError = mockk<WriteError>()
        coEvery { exception.error } returns writerError
        coEvery { writerError.category } returns ErrorCategory.DUPLICATE_KEY

        // Act
        val (status, message) = todoExceptionMapper.toErrorResponse(exception)

        // Assert
        assertEquals(status, HttpStatusCode.Conflict)
        assert(message == "Item already exists")
    }

    @Test
    fun `returns InternalServerError when MongoWriteException is thrown with other category`() {
        // Arrange
        val exception = mockk<MongoWriteException>(relaxed = true)
        val writerError = mockk<WriteError>()
        coEvery { exception.error } returns writerError
        coEvery { writerError.category } returns ErrorCategory.UNCATEGORIZED

        // Act
        val (status, message) = todoExceptionMapper.toErrorResponse(exception)

        // Assert
        assertEquals(status, HttpStatusCode.InternalServerError)
        assert(message == "Database write error")
    }

    @Test
    fun `returns InternalServerError when MongoException is thrown`() {
        // Arrange
        val exception = mockk<MongoException>(relaxed = true)

        // Act
        val (status, message) = todoExceptionMapper.toErrorResponse(exception)

        // Assert
        assertEquals(status, HttpStatusCode.InternalServerError)
        assert(message == "Database error")
    }

    @Test
    fun `returns InternalServerError when unknown exception is thrown`() {
        // Arrange
        val exception = mockk<Exception>(relaxed = true)

        // Act
        val (status, message) = todoExceptionMapper.toErrorResponse(exception)

        // Assert
        assertEquals(status, HttpStatusCode.InternalServerError)
        assert(message == "Unknown error")
    }
}
