package com.appsheaven.keepup.database

import com.appsheaven.keepup.todos.entities.Todo
import org.bson.types.ObjectId

internal object MongoDatabaseFixtures {

    val anyTodo: Todo = Todo(ObjectId(), "Title", "Description", false)
}
