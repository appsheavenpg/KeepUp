package com.appsheaven.keepup.todo.infrastructure

import com.appsheaven.keepup.todo.data.models.NetworkTodo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoRequest(
    @SerialName("item")
    val item: NetworkTodo
)
