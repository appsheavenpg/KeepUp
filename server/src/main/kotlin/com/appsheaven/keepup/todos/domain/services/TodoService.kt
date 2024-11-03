package com.appsheaven.keepup.todos.domain.services

import com.appsheaven.keepup.todo.data.models.NetworkTodo
import com.appsheaven.keepup.todos.domain.entities.PagedTodos

interface TodoService {
    suspend fun getTodos(page: Int, size: Int): PagedTodos
    suspend fun insert(todo: NetworkTodo)
    suspend fun update(todo: NetworkTodo)
    suspend fun delete(todo: NetworkTodo)
}
