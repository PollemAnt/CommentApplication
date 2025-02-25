package com.example.test.repository.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.time.LocalDateTime
import kotlin.random.Random

interface CommentService {

    @GET("/comments")
    suspend fun loadAll(): List<CommentDto>

    @DELETE("/comments/{id}")
    suspend fun deleteComment(@Path("id") id: Int)

    @POST("/comments")
    suspend fun addComment(@Body comment: CommentDto): CommentDto
}

data class CommentDto(
    val id: Int,
    val body: String,
    ) {

    val date: LocalDateTime
        get() {
            return randomDate()
        }
}

private fun randomDate(): LocalDateTime = LocalDateTime.of(
    2023,
    Random.nextInt(1, 12),
    Random.nextInt(1, 28),
    Random.nextInt(0, 23),
    Random.nextInt(0, 59),
    )

object RetrofitProvider {
    private val retorfit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    val commentService: CommentService by lazy { retorfit.create(CommentService::class.java) }

}