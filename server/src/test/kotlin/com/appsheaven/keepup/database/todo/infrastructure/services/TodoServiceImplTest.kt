package com.appsheaven.keepup.database.todo.infrastructure.services

import com.appsheaven.keepup.database.todo.infrastructure.NetworkTodoFixtures
import com.appsheaven.keepup.todos.domain.entities.toTodo
import com.appsheaven.keepup.todos.domain.repositories.TodoRepository
import com.appsheaven.keepup.todos.infrastructure.services.TodoServiceImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class TodoServiceImplTest {

    private val repository = mockk<TodoRepository>(relaxed = true)
    private val sut = TodoServiceImpl(repository)

    @Test
    fun `should get todos`() = runTest {
        // Arrange
        val page = 1
        val size = 10

        // Act
        sut.getTodos(page, size)

        // Assert
        coVerify(exactly = 1) { repository.getPagedTodos(page, size) }
    }

    @Test
    fun `should rethrow error when get todos fails`() = runTest {
        // Arrange
        val page = 1
        val size = 10
        coEvery { repository.getPagedTodos(page, size) } throws Exception()

        // Act
        val result = kotlin.runCatching { sut.getTodos(page, size) }

        // Assert
        assert(result.isFailure)
    }

    @Test
    fun `should insert todo`() = runTest {
        // Arrange
        val todo = NetworkTodoFixtures.anyNetworkTodo

        // Act
        sut.insert(todo)

        // Assert
        coVerify(exactly = 1) { repository.insert(todo.toTodo()) }
    }

    @Test
    fun `should rethrow error when insert todo fails`() = runTest {
        // Arrange
        val todo = NetworkTodoFixtures.anyNetworkTodo
        coEvery { repository.insert(todo.toTodo()) } throws Exception()

        // Act
        val result = kotlin.runCatching { sut.insert(todo) }

        // Assert
        assert(result.isFailure)
    }

    @Test
    fun `should update todo`() = runTest {
        // Arrange
        val todo = NetworkTodoFixtures.anyNetworkTodo

        // Act
        sut.update(todo)

        // Assert
        coVerify(exactly = 1) { repository.update(todo.toTodo()) }
    }

    @Test
    fun `should rethrow error when update todo fails`() = runTest {
        // Arrange
        val todo = NetworkTodoFixtures.anyNetworkTodo
        coEvery { repository.update(todo.toTodo()) } throws Exception()

        // Act
        val result = kotlin.runCatching { sut.update(todo) }

        // Assert
        assert(result.isFailure)
    }

    @Test
    fun `should delete todo`() = runTest {
        // Arrange
        val todo = NetworkTodoFixtures.anyNetworkTodo

        // Act
        sut.delete(todo)

        // Assert
        coVerify(exactly = 1) { repository.delete(todo.id) }
    }

    @Test
    fun `should rethrow error when delete todo fails`() = runTest {
        // Arrange
        val todo = NetworkTodoFixtures.anyNetworkTodo
        coEvery { repository.delete(todo.id) } throws Exception()

        // Act
        val result = kotlin.runCatching { sut.delete(todo) }

        // Assert
        assert(result.isFailure)
    }
}
