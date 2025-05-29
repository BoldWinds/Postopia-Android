package com.postopia.domain.repository

import com.postopia.data.model.FeedPostInfo
import com.postopia.data.model.Result
import com.postopia.data.model.UserPostInfo
import kotlinx.coroutines.flow.Flow

interface PostRepository{

    suspend fun getUserPosts(userId: String): Flow<Result<List<UserPostInfo>>>

    suspend fun getPopularPosts(page: Int, size: String? = null): Flow<Result<List<FeedPostInfo>>>

}