package com.appsheaven.keepup.database.todo.infrastructure.persistence

import com.appsheaven.keepup.database.todo.domain.TodoFixtures
import com.appsheaven.keepup.todos.infrastructure.persistence.MongoTodoDatabase
import com.mongodb.MongoException
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.test.runTest
import org.bson.BsonObjectId
import org.bson.types.ObjectId
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MongoTodoDatabaseTest {

    private lateinit var dbContainer: MongoDBContainer
    private lateinit var client: MongoClient
    private lateinit var sut: MongoTodoDatabase

    @BeforeTest
    fun before() {
        dbContainer = MongoDBContainer(DockerImageName.parse("mongo:latest")).apply { start() }
        client = MongoClient.create(dbContainer.connectionString)
        sut = MongoTodoDatabase(client)
    }

    @AfterTest
    fun after() {
        dbContainer.stop()
    }

    @Test
    fun `inserts new todo`() = runTest {
        val todo = TodoFixtures.anyTodo

        val actual = sut.insertTodo(todo)

        assertEquals(BsonObjectId(todo._id), actual.insertedId)
        assertEquals(true, actual.wasAcknowledged())
    }

    @Test
    fun `throws on inserting todo with error`() = runTest {
        val todo = TodoFixtures.anyTodo

        sut.insertTodo(todo)

        assertFailsWith<MongoException> { sut.insertTodo(todo) }
    }

    @Test
    fun `gets all todos`() = runTest {
        val todo = TodoFixtures.anyTodo
        val anotherTodo = todo.copy(_id = ObjectId())

        sut.insertTodo(todo)
        sut.insertTodo(anotherTodo)

        val actual = sut.getTodos(page = 1, pageSize = 10)

        assertEquals(listOf(todo, anotherTodo), actual)
    }

    @Test
    fun `returns empty list when no todos`() = runTest {
        val actual = sut.getTodos(page = 1, pageSize = 10)

        assertEquals(emptyList(), actual)
    }

    @Test
    fun `gets todo with pagination for page 1`() = runTest {
        val todo = TodoFixtures.anyTodo
        val anotherTodo = todo.copy(_id = ObjectId())

        sut.insertTodo(todo)
        sut.insertTodo(anotherTodo)

        val actual = sut.getTodos(page = 1, pageSize = 1)

        assertEquals(listOf(todo), actual)
    }

    @Test
    fun `gets todo with pagination for page 2`() = runTest {
        val todo = TodoFixtures.anyTodo
        val anotherTodo = todo.copy(_id = ObjectId())

        sut.insertTodo(todo)
        sut.insertTodo(anotherTodo)

        val actual = sut.getTodos(page = 2, pageSize = 1)

        assertEquals(listOf(anotherTodo), actual)
    }

    @Test
    fun `return empty list with single todo for page 2`() = runTest {
        val todo = TodoFixtures.anyTodo

        sut.insertTodo(todo)

        val actual = sut.getTodos(page = 2, pageSize = 10)

        assertEquals(emptyList(), actual)
    }

    @Test
    fun `puts todo`() = runTest {
        val todo = TodoFixtures.anyTodo

        sut.insertTodo(todo)

        val updatedTodo = todo.copy(title = "Updated title")
        val actual = sut.putTodo(updatedTodo)

        assertEquals(1, actual.matchedCount)
        assertEquals(1, actual.modifiedCount)
    }

    @Test
    fun `deletes todo by id`() = runTest {
        val todo = TodoFixtures.anyTodo

        sut.insertTodo(todo)

        val actual = sut.deleteTodoById(todo._id)

        assertEquals(1, actual.deletedCount)
    }

    @Test
    fun `deletes todo by id with no match`() = runTest {
        val todo = TodoFixtures.anyTodo

        sut.insertTodo(todo)

        val actual = sut.deleteTodoById(ObjectId())

        assertEquals(0, actual.deletedCount)
    }
}
