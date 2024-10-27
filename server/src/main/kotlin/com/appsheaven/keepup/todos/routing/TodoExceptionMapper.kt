package com.appsheaven.keepup.todos.routing

import com.appsheaven.keepup.core.ErrorResponse
import com.appsheaven.keepup.core.ExceptionMapper
import com.mongodb.ErrorCategory
import com.mongodb.MongoException
import com.mongodb.MongoWriteException
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.ContentTransformationException
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException

class TodoExceptionMapper : ExceptionMapper {
    override fun toErrorResponse(exception: Throwable): ErrorResponse {
        return when (exception) {
            is ContentTransformationException -> HttpStatusCode.BadRequest to "Invalid request data"
            is SerializationException -> HttpStatusCode.BadRequest to "Serialization error"
            is BadRequestException -> HttpStatusCode.BadRequest to "Bad request"
            is IOException -> HttpStatusCode.InternalServerError to "I/O error"
            is MongoWriteException -> {
                when (exception.error.category) {
                    ErrorCategory.DUPLICATE_KEY -> HttpStatusCode.Conflict to "Item already exists"
                    else -> HttpStatusCode.InternalServerError to "Database write error"
                }
            }

            is MongoException -> HttpStatusCode.InternalServerError to "Database error"
            else -> HttpStatusCode.InternalServerError to "Unknown error"
        }
    }
}
