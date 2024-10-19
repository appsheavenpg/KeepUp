package com.appsheaven.keepup.database.todo.domain

import com.appsheaven.keepup.todos.domain.entities.Todo
import org.bson.types.ObjectId

internal object TodoFixtures {

    val anyTodo: Todo = Todo(ObjectId(), "Title", "Description", false)

}
