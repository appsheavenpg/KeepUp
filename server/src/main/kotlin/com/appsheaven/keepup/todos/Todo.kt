package com.appsheaven.keepup.todos

import org.bson.types.ObjectId

internal data class Todo(
    @Suppress("PropertyName") val _id: ObjectId,
    val title: String,
    val description: String,
    val completed: Boolean
)
