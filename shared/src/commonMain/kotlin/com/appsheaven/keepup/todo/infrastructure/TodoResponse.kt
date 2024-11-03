package com.appsheaven.keepup.todo.infrastructure

import com.appsheaven.keepup.todo.data.models.NetworkTodo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoResponse(
    @SerialName("items")
    val todos: List<NetworkTodo>,
    @SerialName("current_page")
    val currentPage: Int,
    @SerialName("page_size")
    val pageSize: Int,
    @SerialName("has_more")
    val hasMore: Boolean
)
