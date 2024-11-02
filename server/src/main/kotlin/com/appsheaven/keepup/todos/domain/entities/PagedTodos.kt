package com.appsheaven.keepup.todos.domain.entities

import com.appsheaven.keepup.todo.data.models.NetworkTodo

data class PagedTodos(
    val items: List<NetworkTodo>,
    val hasMore: Boolean
)
