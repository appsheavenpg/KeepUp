package com.appsheaven.keepup.core

import io.ktor.http.HttpStatusCode

typealias ErrorResponse = Pair<HttpStatusCode, String>

fun interface ExceptionMapper {
    fun toErrorResponse(exception: Throwable): ErrorResponse
}
