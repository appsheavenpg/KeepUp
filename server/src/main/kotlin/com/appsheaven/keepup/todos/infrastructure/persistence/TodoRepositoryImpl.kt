package com.appsheaven.keepup.todos.infrastructure.persistence

import com.appsheaven.keepup.todos.domain.services.TodoDatabase
import com.appsheaven.keepup.todos.domain.repositories.TodoRepository
import com.appsheaven.keepup.todos.domain.entities.Todo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.bson.types.ObjectId

internal class TodoRepositoryImpl(
    private val database: TodoDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TodoRepository {

    override suspend fun getPagedTodos(page: Int, size: Int): List<Todo> {
        with(dispatcher) {
            return database.getTodos(page, size)
        }
    }

    override suspend fun insert(todo: Todo) {
        with(dispatcher) {
            database.insertTodo(todo)
        }
    }

    override suspend fun update(todo: Todo) {
        with(dispatcher) {
            database.putTodo(todo)
        }
    }

    override suspend fun delete(id: String) {
        with(dispatcher) {
            database.deleteTodoById(id.toObjectId())
        }
    }
}

private fun String.toObjectId(): ObjectId {
    return ObjectId(this)
}
