package com.postopia.domain.repository

import com.postopia.data.model.Result
import com.postopia.data.model.SpaceVoteInfo
import kotlinx.coroutines.flow.Flow

interface VoteRepository{

    suspend fun getSpaceVotes(spaceId: Long): Flow<Result<List<SpaceVoteInfo>>>

    suspend fun deleteComment(
        postId: Long,
        userId: Long,
        spaceId: Long,
        commentId: Long,
        commentContent: String
    ) : Flow<Result<Unit>>

    suspend fun pinComment(
        postId: Long,
        userId: Long,
        spaceId: Long,
        commentId: Long,
        commentContent: String
    ) : Flow<Result<Unit>>

    suspend fun unpinComment(
        postId: Long,
        userId: Long,
        spaceId: Long,
        commentId: Long,
        commentContent: String
    ) : Flow<Result<Unit>>

    suspend fun deletePost(
        postId: Long,
        userId: Long,
        spaceId: Long,
        postSubject: String
    ) : Flow<Result<Unit>>

    suspend fun archivePost(
        postId: Long,
        userId: Long,
        spaceId: Long,
        postSubject: String
    ) : Flow<Result<Unit>>

    suspend fun unarchivePost(
        postId: Long,
        userId: Long,
        spaceId: Long,
        postSubject: String
    ) : Flow<Result<Unit>>

    suspend fun muteUser(
        spaceId: Long,
        userId: Long,
        username: String,
        reason: String
    ) : Flow<Result<Unit>>

    suspend fun expelUser(
        spaceId: Long,
        userId: Long,
        username: String,
        reason: String
    ) : Flow<Result<Unit>>
}