package com.togeda.app.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class PostsResponse(
    val data        : List<PostResponseDto>,
    val lastPage    : Boolean,
    val listCount   : Long
)

data class PostResponseDto(
    val id                          : String?,
    val title                       : String?,
    val images                      : List<String>?,
    val description                 : String?,
    val maximumPeople               : Int?,
    val location                    : BaseLocation?,
    val toDate                      : String?,
    val fromDate                    : String?,
    val createdAt                   : String?,
    val interests                   : List<Interest>?,
    val owner                       : MiniUser?,
    val payment                     : Double?,
    val currency                    : Currency?,
    val currentUserStatus           : String?,
    val currentUserRole             : String?,
    val currentUserArrivalStatus    : String?,
    val accessibility               : String?,
    val askToJoin                   : Boolean?,
    val needsLocationalConfirmation : Boolean?,
    val rating                      : Double?,
    val clubId                      : String?,
    val participantsCount           : Int?,
    val status                      : String?,
    val savedByCurrentUser          : Boolean?,
    val chatRoomId                  : String?,
    val blockedForCurrentUser       : Boolean?
)

data class BaseLocation(
    val name        : String?,
    val address     : String?,
    val city        : String?,
    val state       : String?,
    val country     : String?,
    val latitude    : Double?,
    val longitude   : Double?
)

data class Interest(
    val name        : String?,
    val icon        : String?,
    val category    : String?
)

data class MiniUser(
    val id              : String?,
    val firstName       : String?,
    val lastName        : String?,
    val profilePhotos   : List<String>?,
    val occupation      : String?
)

data class Currency(
    val id      : String?,
    val name    : String?,
    val symbol  : String?
)

interface PostsApi {
    @GET("posts")
    suspend fun getAllPosts(
        @Query("sortBy") sortBy         : String,
        @Query("longitude") longitude   : Double,
        @Query("latitude") latitude     : Double,
        @Query("distance") distance     : Int,
        @Query("pageNumber") pageNumber : Int,
        @Query("pageSize") pageSize     : Int,
        @Query("categories") categories : List<String>? = null,
        @Query("toDate") toDate         : String?       = null,
        @Query("fromDate") fromDate     : String?       = null
    ): PostsResponse

    @GET("posts/{postId}")
    suspend fun getPostById(@Path("postId") postId: String): PostResponseDto
} 