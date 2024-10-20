package com.appsheaven.keepup.todos.domain.services

import com.appsheaven.keepup.todo.data.models.NetworkTodo

interface TodoService {
    suspend fun getTodos(page: Int, size: Int): List<NetworkTodo>
    suspend fun insert(todo: NetworkTodo)
    suspend fun update(todo: NetworkTodo)
    suspend fun delete(todo: NetworkTodo)
}
