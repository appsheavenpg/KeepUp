package com.appsheaven.keepup.todos.domain.services

import com.appsheaven.keepup.todos.domain.entities.Todo
import com.mongodb.MongoException
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import org.bson.types.ObjectId

internal interface TodoDatabase {
    @Throws(MongoException::class)
    suspend fun insertTodo(todo: Todo): InsertOneResult
    suspend fun getTodos(page: Int, pageSize: Int): List<Todo>
    suspend fun putTodo(todo: Todo): UpdateResult
    suspend fun deleteTodoById(id: ObjectId): DeleteResult
}
