package com.appsheaven.keepup.todos

import org.bson.types.ObjectId

internal data class Todo(
    val id: ObjectId,
    val title: String,
    val description: String,
    val completed: Boolean
)
