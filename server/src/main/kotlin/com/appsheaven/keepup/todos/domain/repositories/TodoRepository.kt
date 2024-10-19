package com.appsheaven.keepup.todos.domain.repositories

import com.appsheaven.keepup.todos.domain.entities.Todo

internal interface TodoRepository {
    suspend fun getPagedTodos(page: Int, size: Int): List<Todo>
    suspend fun insert(todo: Todo)
    suspend fun update(todo: Todo)
    suspend fun delete(id: String)
}
