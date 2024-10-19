package com.appsheaven.keepup.database.todo.infrastructure.persistence

import com.appsheaven.keepup.database.todo.domain.TodoFixtures
import com.appsheaven.keepup.todos.domain.services.TodoDatabase
import com.appsheaven.keepup.todos.infrastructure.persistence.TodoRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import kotlin.test.Test

class TodoRepositoryImplTest {
    private val todoDatabase = mockk<TodoDatabase>(relaxed = true)
    private val dispatcher = StandardTestDispatcher()
    private val sut = TodoRepositoryImpl(todoDatabase, dispatcher)

    @Test
    fun `should get paged todos`() = runTest {
        // Arrange
        val page = 1
        val size = 10

        // Act
        sut.getPagedTodos(page, size)

        // Assert
        coVerify(exactly = 1) { todoDatabase.getTodos(page, size) }
    }

    @Test
    fun `should throw exception when getting paged todos`() = runTest {
        // Arrange
        val page = 1
        val size = 10
        coEvery { todoDatabase.getTodos(page, size) } throws Exception()

        // Act
        val result = kotlin.runCatching { sut.getPagedTodos(page, size) }

        // Assert
        assert(result.isFailure)
    }

    @Test
    fun `should insert todo`() = runTest {
        // Arrange
        val todo = TodoFixtures.anyTodo

        // Act
        sut.insert(todo)

        // Assert
        coVerify(exactly = 1) { todoDatabase.insertTodo(todo) }
    }

    @Test
    fun `should throw exception when inserting todo`() = runTest {
        // Arrange
        val todo = TodoFixtures.anyTodo
        coEvery { todoDatabase.insertTodo(todo) } throws Exception()

        // Act
        val result = kotlin.runCatching { sut.insert(todo) }

        // Assert
        assert(result.isFailure)
    }

    @Test
    fun `should update todo`() = runTest {
        // Arrange
        val todo = TodoFixtures.anyTodo

        // Act
        sut.update(todo)

        // Assert
        coVerify(exactly = 1) { todoDatabase.putTodo(todo) }
    }

    @Test
    fun `should throw exception when updating todo`() = runTest {
        // Arrange
        val todo = TodoFixtures.anyTodo
        coEvery { todoDatabase.putTodo(todo) } throws Exception()

        // Act
        val result = kotlin.runCatching { sut.update(todo) }

        // Assert
        assert(result.isFailure)
    }

    @Test
    fun `should delete todo`() = runTest {
        // Arrange
        val id = ObjectId().toHexString()

        // Act
        sut.delete(id)

        // Assert
        coVerify(exactly = 1) { todoDatabase.deleteTodoById(ObjectId(id)) }
    }

    @Test
    fun `should throw exception when deleting todo`() = runTest {
        // Arrange
        val id = ObjectId().toHexString()
        coEvery { todoDatabase.deleteTodoById(ObjectId(id)) } throws Exception()

        // Act
        val result = kotlin.runCatching { sut.delete(id) }

        // Assert
        assert(result.isFailure)
    }
}
