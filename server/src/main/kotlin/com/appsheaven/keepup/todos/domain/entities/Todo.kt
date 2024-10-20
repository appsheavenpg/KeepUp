package com.appsheaven.keepup.todos.domain.entities

import org.bson.types.ObjectId
import com.appsheaven.keepup.todo.data.models.NetworkTodo

internal data class Todo(
    @Suppress("PropertyName") val _id: ObjectId,
    val title: String,
    val description: String,
    val completed: Boolean
)

internal fun Todo.toNetworkTodo() = NetworkTodo(
    id = _id.toHexString(),
    title = title,
    description = description,
    completed = completed
)

internal fun NetworkTodo.toTodo() = Todo(
    _id = ObjectId(id),
    title = title,
    description = description,
    completed = completed
)
