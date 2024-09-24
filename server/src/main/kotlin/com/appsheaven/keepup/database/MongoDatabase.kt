package com.appsheaven.keepup.database

import com.appsheaven.keepup.core.DatabaseOperationResult
import com.appsheaven.keepup.core.KeepUpDatabase
import com.appsheaven.keepup.todos.Todo
import com.mongodb.client.model.InsertOneOptions
import com.mongodb.kotlin.client.coroutine.MongoClient

internal class MongoDatabase(
    mongoClient: MongoClient,
    databaseName: String = "keep-up",
    collectionName: String = "todos"
) : KeepUpDatabase {

    private val database = mongoClient.getDatabase(databaseName)
    private val collection = database.getCollection<Todo>(collectionName)

    override suspend fun insertTodo(todo: Todo): DatabaseOperationResult {
        val result = collection.insertOne(
            todo,
            options = InsertOneOptions()
        )

        return result.wasAcknowledged()
    }
}