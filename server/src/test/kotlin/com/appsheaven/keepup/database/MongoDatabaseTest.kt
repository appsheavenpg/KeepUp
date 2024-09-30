package com.appsheaven.keepup.database

import com.mongodb.MongoException
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.test.runTest
import org.bson.BsonObjectId
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MongoDatabaseTest {

    private lateinit var dbContainer: MongoDBContainer
    private lateinit var client: MongoClient
    private lateinit var sut: KeepUpMongoDatabase

    @BeforeTest
    fun before() {
        dbContainer = MongoDBContainer(DockerImageName.parse("mongo:latest")).apply { start() }
        client = MongoClient.create(dbContainer.connectionString)
        sut = KeepUpMongoDatabase(client)
    }

    @AfterTest
    fun after() {
        dbContainer.stop()
    }

    @Test
    fun `inserts new todo`() = runTest {
        val todo = MongoDatabaseFixtures.anyTodo

        val actual = sut.insertTodo(todo)

        assertEquals(BsonObjectId(todo._id), actual.insertedId)
        assertEquals(true, actual.wasAcknowledged())
    }

    @Test
    fun `throws on inserting todo with error`() = runTest {
        val todo = MongoDatabaseFixtures.anyTodo

        sut.insertTodo(todo)

        assertFailsWith<MongoException> { sut.insertTodo(todo) }
    }
}
