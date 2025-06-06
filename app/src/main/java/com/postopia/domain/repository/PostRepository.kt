package com.postopia.domain.repository

import com.postopia.data.model.FeedPostInfo
import com.postopia.data.model.PostInfo
import com.postopia.data.model.Result
import com.postopia.data.model.UserPostInfo
import kotlinx.coroutines.flow.Flow

interface PostRepository{

    suspend fun getUserPosts(page: Int, userId : String? = null): Flow<Result<List<UserPostInfo>>>

    suspend fun getPopularPosts(page: Int, size: String? = null): Flow<Result<List<FeedPostInfo>>>

    suspend fun getSpacePosts(spaceId: Long, page: Int = 0, size: String? = null): Flow<Result<List<PostInfo>>>

    suspend fun getPostByID(postID: Long) : Flow<Result<PostInfo>>

    suspend fun createPost(
        spaceId: Long,
        spaceName: String,
        subject: String,
        content: String
    ) : Flow<Result<Unit>>

}