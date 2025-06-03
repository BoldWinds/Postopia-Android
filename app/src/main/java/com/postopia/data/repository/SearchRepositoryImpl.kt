package com.postopia.data.repository

import com.postopia.data.model.FeedPostInfo
import com.postopia.data.model.GeneralCommentInfo
import com.postopia.data.model.Result
import com.postopia.data.model.SpaceInfo
import com.postopia.data.model.SpacePart
import com.postopia.data.model.UserInfo
import com.postopia.data.remote.CommentRemoteDataSource
import com.postopia.data.remote.PostRemoteDataSource
import com.postopia.data.remote.SearchRemoteDataSource
import com.postopia.data.remote.SpaceRemoteDataSource
import com.postopia.data.remote.UserRemoteDataSource
import com.postopia.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val remoteDataSource: SearchRemoteDataSource,
    private val postRemoteDataSource: PostRemoteDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val spaceRemoteDataSource: SpaceRemoteDataSource,
    private val commentRemoteDataSource: CommentRemoteDataSource,
) : SearchRepository {
    override suspend fun searchPosts(
        query: String,
        page: Int,
        size: Int?
    ): Flow<Result<List<FeedPostInfo>>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteDataSource.searchPost(query, page, size)
            if (response.isSuccessful()) {
                val postDocs = response.requireData().requireData()
                if(postDocs.isEmpty()){
                    emit(Result.Success(emptyList()))
                    return@flow
                }
                val ids = postDocs.map { it.id }
                val postsResponse = postRemoteDataSource.searchPosts(ids)
                if (postsResponse.isSuccessful()) {
                    emit(Result.Success(postsResponse.requireData()))
                }else{
                    emit(Result.Error(Exception(postsResponse.message)))
                }
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }

    override suspend fun searchUsers(
        query: String,
        page: Int,
        size: Int?
    ): Flow<Result<List<UserInfo>>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteDataSource.searchUser(query, page, size)
            if (response.isSuccessful()) {
                val userDocs = response.requireData().requireData()
                if(userDocs.isEmpty()){
                    emit(Result.Success(emptyList()))
                    return@flow
                }
                val ids = userDocs.map { it.id }
                val usersResponse = userRemoteDataSource.searchUserInfos(ids)
                if (usersResponse.isSuccessful()) {
                    emit(Result.Success(usersResponse.requireData()))
                }else{
                    emit(Result.Error(Exception(usersResponse.message)))
                }
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }

    override suspend fun searchSpaces(
        query: String,
        page: Int,
        size: Int?
    ): Flow<Result<List<SpaceInfo>>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteDataSource.searchSpace(query, page, size)
            if (response.isSuccessful()) {
                val spaceDocs = response.requireData().requireData()
                if(spaceDocs.isEmpty()){
                    emit(Result.Success(emptyList()))
                    return@flow
                }
                val ids = spaceDocs.map { it.id }
                val spaceResponse = spaceRemoteDataSource.searchSpaceInfos(ids)
                if (spaceResponse.isSuccessful()) {
                    val searchSpaceInfos = spaceResponse.requireData()
                    val spaceInfos = searchSpaceInfos.map { searchSpaceInfo ->
                        val spaceDoc = spaceDocs.find { it.id.toLong() == searchSpaceInfo.space.id }
                        SpaceInfo(
                            space = SpacePart(
                                id = searchSpaceInfo.space.id,
                                name = spaceDoc!!.name,
                                avatar = searchSpaceInfo.space.avatar,
                                createdAt = searchSpaceInfo.space.createdAt,
                                description = spaceDoc.description,
                                memberCount = searchSpaceInfo.space.memberCount,
                                postCount = searchSpaceInfo.space.postCount
                            ),
                            isMember = searchSpaceInfo.isMember
                        )
                    }
                    emit(Result.Success(spaceInfos))
                } else {
                    emit(Result.Error(Exception(spaceResponse.message)))
                }
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun searchComments(
        query: String,
        page: Int,
        size: Int?
    ): Flow<Result<List<GeneralCommentInfo>>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteDataSource.searchComment(query, page, size)
            if (response.isSuccessful()) {
                val commentDocs = response.requireData().requireData()
                if(commentDocs.isEmpty()){
                    emit(Result.Success(emptyList()))
                    return@flow
                }
                val ids = commentDocs.map { it.id }
                val searchResponse = commentRemoteDataSource.searchComments(ids)
                if (searchResponse.isSuccessful()) {
                    val searchCommentInfos = searchResponse.requireData()
                    val data = searchCommentInfos.mapIndexed { index, searchCommentInfo ->
                        val commentDoc = commentDocs.find { it.id == searchCommentInfo.comment.id.toString() }
                        if(commentDoc == null){
                            throw Exception("Comment document not found for ID: ${searchCommentInfo.comment.id}")
                            return@flow
                        }else{
                            GeneralCommentInfo(
                                commentId = searchCommentInfo.comment.id,
                                spaceId = commentDoc.spaceId,
                                spaceName = searchCommentInfo.post.spaceName,
                                postId = commentDoc.postId,
                                postSubject = searchCommentInfo.post.subject,
                                authorId = commentDoc.userId,
                                authorName = searchCommentInfo.user.nickname,
                                authorAvatar = searchCommentInfo.user.avatar,
                                createdAt = searchCommentInfo.comment.createdAt,
                                content = commentDoc.content
                            )
                        }
                    }
                    emit(Result.Success(data))
                }else{
                    emit(Result.Error(Exception(searchResponse.message)))
                }
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }

}
