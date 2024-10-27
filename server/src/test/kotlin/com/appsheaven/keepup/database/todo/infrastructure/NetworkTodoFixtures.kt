package com.appsheaven.keepup.database.todo.infrastructure

import com.appsheaven.keepup.todo.data.models.NetworkTodo
import org.bson.types.ObjectId

object NetworkTodoFixtures {
    val anyNetworkTodo = NetworkTodo(
        id = ObjectId().toHexString(),
        title = "title",
        description = "description",
        completed = false
    )

    val anyNetworkTodoRequestJson =
        """
        {
            "item": {
                "id": "${anyNetworkTodo.id}",
                "title": "${anyNetworkTodo.title}",
                "description": "${anyNetworkTodo.description}",
                "completed": ${anyNetworkTodo.completed}
            }
        }
        """.trimIndent()
}
