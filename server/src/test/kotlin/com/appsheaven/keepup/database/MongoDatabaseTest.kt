package com.appsheaven.keepup.database

import com.appsheaven.keepup.todos.Todo
import com.mongodb.MongoException
import com.mongodb.client.result.InsertOneResult
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.bson.BsonObjectId
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MongoDatabaseTest {

    private lateinit var client: MongoClient
    private lateinit var database: MongoDatabase
    private lateinit var collection: MongoCollection<Todo>
    private lateinit var sut: KeepUpMongoDatabase

    @BeforeTest
    fun before() {
        client = mockk()
        database = mockk()
        collection = mockk()

        coEvery { client.getDatabase(any()) } returns database
        coEvery { database.getCollection<Todo>(any()) } returns collection

        sut = KeepUpMongoDatabase(client)
    }

    @AfterTest
    fun after() {
        clearMocks(client, database, collection)
    }

    @Test
    fun `inserts new todo`() = runTest {
        val todo = MongoDatabaseFixtures.anyTodo
        val insertResult = mockk<InsertOneResult>()
        coEvery { insertResult.wasAcknowledged() } returns true
        coEvery { insertResult.insertedId } returns BsonObjectId(todo.id)
        coEvery { collection.insertOne(todo, any()) } returns insertResult

        val actual = sut.insertTodo(todo)

        assertEquals(BsonObjectId(todo.id), actual.insertedId)
        assertEquals(true, actual.wasAcknowledged())
    }

    @Test
    fun `throws on inserting todo with error`() = runTest {
        val todo = MongoDatabaseFixtures.anyTodo
        coEvery { collection.insertOne(todo, any()) } throws MongoException("Exception")

        assertFailsWith<MongoException> { collection.insertOne(todo) }
    }
}
