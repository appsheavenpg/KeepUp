package com.appsheaven.keepup.todos.infrastructure.persistence

import com.appsheaven.keepup.todos.domain.services.TodoDatabase
import com.appsheaven.keepup.todos.domain.entities.Todo
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.InsertOneOptions
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

internal class MongoTodoDatabase(
    mongoClient: MongoClient,
    databaseName: String = "keep-up",
    collectionName: String = "todos"
) : TodoDatabase {

    private val database = mongoClient.getDatabase(databaseName)
    private val collection = database.getCollection<Todo>(collectionName)

    @Throws(MongoException::class)
    override suspend fun insertTodo(todo: Todo): InsertOneResult {
        return collection.insertOne(todo, options = InsertOneOptions())
    }

    override suspend fun getTodos(page: Int, pageSize: Int): List<Todo> {
        val currentPage = (page - 1).takeIf { it >= 0 } ?: 0
        val skip = currentPage * pageSize
        return collection
            .find()
            .skip(skip)
            .limit(pageSize)
            .toList()
    }

    @Throws(MongoException::class)
    override suspend fun putTodo(todo: Todo): UpdateResult {
        val filter = Filters.eq("_id", todo._id)
        return collection.replaceOne(filter, todo)
    }

    override suspend fun deleteTodoById(id: ObjectId): DeleteResult {
        val filter = Filters.eq("_id", id)
        return collection.deleteOne(filter)
    }
}
