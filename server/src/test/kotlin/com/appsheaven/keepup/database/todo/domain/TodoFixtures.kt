package com.appsheaven.keepup.database.todo.domain

import com.appsheaven.keepup.todos.domain.entities.Todo
import org.bson.types.ObjectId

internal object TodoFixtures {

    val anyTodo: Todo = Todo(ObjectId(), "Title", "Description", false)

    val fiveTodos = listOf(
        Todo(ObjectId(), "Buy groceries", "Get milk, eggs and bread", false),
        Todo(ObjectId(), "Call mom", "Weekly catch-up call", true),
        Todo(ObjectId(), "Fix bug", "Address the login issue", false),
        Todo(ObjectId(), "Gym session", "Leg day workout", false),
        Todo(ObjectId(), "Read book", "Chapter 3 of Clean Code", true)
    )

    val fifteenTodos = listOf(
        Todo(ObjectId(), "Morning run", "5km around the park", false),
        Todo(ObjectId(), "Team meeting", "Sprint planning", true),
        Todo(ObjectId(), "Code review", "Review PR #123", false),
        Todo(ObjectId(), "Dentist appointment", "Annual checkup", false),
        Todo(ObjectId(), "Write documentation", "API endpoints", true),
        Todo(ObjectId(), "Pay bills", "Electricity and water", false),
        Todo(ObjectId(), "Buy birthday gift", "For Sarah's party", false),
        Todo(ObjectId(), "Clean apartment", "Vacuum and dusting", true),
        Todo(ObjectId(), "Update resume", "Add recent projects", false),
        Todo(ObjectId(), "Car maintenance", "Oil change", true),
        Todo(ObjectId(), "Learn Kotlin", "Complete chapter 5", false),
        Todo(ObjectId(), "Backup files", "Work documents", true),
        Todo(ObjectId(), "Plan vacation", "Research destinations", false),
        Todo(ObjectId(), "Fix printer", "Paper jam issue", true),
        Todo(ObjectId(), "Water plants", "Indoor and balcony plants", false)
    )

}
