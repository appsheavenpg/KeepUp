package com.appsheaven.keepup.database

import com.appsheaven.keepup.core.KeepUpDatabase
import com.appsheaven.keepup.todos.Todo
import com.mongodb.MongoException
import com.mongodb.client.model.InsertOneOptions
import com.mongodb.client.result.InsertOneResult
import com.mongodb.kotlin.client.coroutine.MongoClient

internal class KeepUpMongoDatabase(
    mongoClient: MongoClient,
    databaseName: String = "keep-up",
    collectionName: String = "todos"
) : KeepUpDatabase {

    private val database = mongoClient.getDatabase(databaseName)
    private val collection = database.getCollection<Todo>(collectionName)

    @Throws(MongoException::class)
    override suspend fun insertTodo(todo: Todo): InsertOneResult {
        return collection.insertOne(todo, options = InsertOneOptions())
    }
}
