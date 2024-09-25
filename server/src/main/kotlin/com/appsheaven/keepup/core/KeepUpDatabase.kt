package com.appsheaven.keepup.core

import com.appsheaven.keepup.todos.Todo
import com.mongodb.MongoException
import com.mongodb.client.result.InsertOneResult

internal interface KeepUpDatabase {
    @Throws(MongoException::class)
    suspend fun insertTodo(todo: Todo): InsertOneResult
}
