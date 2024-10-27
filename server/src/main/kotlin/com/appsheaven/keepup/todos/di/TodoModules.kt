package com.appsheaven.keepup.todos.di

import com.appsheaven.keepup.core.ExceptionMapper
import com.appsheaven.keepup.todos.domain.repositories.TodoRepository
import com.appsheaven.keepup.todos.domain.services.TodoDatabase
import com.appsheaven.keepup.todos.domain.services.TodoService
import com.appsheaven.keepup.todos.infrastructure.persistence.MongoTodoDatabase
import com.appsheaven.keepup.todos.infrastructure.persistence.TodoRepositoryImpl
import com.appsheaven.keepup.todos.infrastructure.services.TodoServiceImpl
import com.appsheaven.keepup.todos.routing.TodoExceptionMapper
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

const val TodoExceptionMapper = "TodoExceptionMapper"

fun todoModule(mongodbKey: String) = module {
    single<MongoClient> { MongoClient.create(mongodbKey) }
    single<TodoDatabase> { MongoTodoDatabase(get()) } bind TodoDatabase::class
    single<TodoRepository> { TodoRepositoryImpl(get(), Dispatchers.IO) } bind TodoRepository::class
    single<ExceptionMapper>(named(TodoExceptionMapper)) { TodoExceptionMapper() } bind ExceptionMapper::class
    single<TodoService> { TodoServiceImpl(get()) } bind TodoService::class
}
