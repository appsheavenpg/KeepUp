package com.appsheaven.keepup.todos.infrastructure.services

import com.appsheaven.keepup.todo.data.models.NetworkTodo
import com.appsheaven.keepup.todos.domain.entities.toNetworkTodo
import com.appsheaven.keepup.todos.domain.entities.toTodo
import com.appsheaven.keepup.todos.domain.repositories.TodoRepository
import com.appsheaven.keepup.todos.domain.services.TodoService

internal class TodoServiceImpl(private val repository: TodoRepository) : TodoService {

    override suspend fun getTodos(page: Int, size: Int): List<NetworkTodo> {
        val todos = repository.getPagedTodos(page, size)
        val networkTodos = todos.map { it.toNetworkTodo() }
        return networkTodos
    }

    override suspend fun insert(todo: NetworkTodo) {
        repository.insert(todo.toTodo())
    }

    override suspend fun update(todo: NetworkTodo) {
        repository.update(todo.toTodo())
    }

    override suspend fun delete(todo: NetworkTodo) {
        repository.delete(todo.id)
    }
}
