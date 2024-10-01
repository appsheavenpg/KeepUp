package com.appsheaven.keepup.core

import com.appsheaven.keepup.todos.Todo
import com.mongodb.MongoException
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult

internal interface KeepUpDatabase {
    @Throws(MongoException::class)
    suspend fun insertTodo(todo: Todo): InsertOneResult
    suspend fun getTodos(page: Int, pageSize: Int): List<Todo>
    suspend fun putTodo(todo: Todo): UpdateResult
}
