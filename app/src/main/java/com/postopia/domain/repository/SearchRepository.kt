package com.postopia.domain.repository

import com.postopia.data.model.FeedPostInfo
import com.postopia.data.model.Result
import com.postopia.data.model.SearchCommentInfo
import com.postopia.data.model.SpaceInfo
import com.postopia.data.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchPosts(query: String, page: Int = 0, size: Int? = null): Flow<Result<List<FeedPostInfo>>>

    suspend fun searchUsers(query: String, page: Int = 0, size: Int? = null): Flow<Result<List<UserInfo>>>

    suspend fun searchSpaces(query: String, page: Int = 0, size: Int? = null): Flow<Result<List<SpaceInfo>>>

    suspend fun searchComments(query: String, page: Int = 0, size: Int? = null): Flow<Result<List<SearchCommentInfo>>>
}