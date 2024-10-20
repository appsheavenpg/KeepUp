package com.appsheaven.keepup.database.todo.domain.entities

import com.appsheaven.keepup.database.todo.domain.TodoFixtures
import com.appsheaven.keepup.database.todo.infrastructure.NetworkTodoFixtures
import com.appsheaven.keepup.todos.domain.entities.toNetworkTodo
import com.appsheaven.keepup.todos.domain.entities.toTodo
import kotlin.test.Test

class TodoTest {

    @Test
    fun `should map Todo to NetworkTodo`() {
        // Arrange
        val todo = TodoFixtures.anyTodo

        // Act
        val networkTodo = todo.toNetworkTodo()

        // Assert
        assert(networkTodo.id == todo._id.toHexString())
        assert(networkTodo.title == todo.title)
        assert(networkTodo.description == todo.description)
        assert(networkTodo.completed == todo.completed)
    }

    @Test
    fun `should map NetworkTodo to Todo`() {
        // Arrange
        val networkTodo = NetworkTodoFixtures.anyNetworkTodo

        // Act
        val todo = networkTodo.toTodo()

        // Assert
        assert(todo._id.toHexString() == networkTodo.id)
        assert(todo.title == networkTodo.title)
        assert(todo.description == networkTodo.description)
        assert(todo.completed == networkTodo.completed)
    }
}
