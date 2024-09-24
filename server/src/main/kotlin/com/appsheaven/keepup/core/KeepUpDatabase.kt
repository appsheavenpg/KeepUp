package com.appsheaven.keepup.core

import com.appsheaven.keepup.todos.Todo

internal typealias DatabaseOperationResult = Boolean

internal interface KeepUpDatabase {
    suspend fun insertTodo(todo: Todo): DatabaseOperationResult
}
