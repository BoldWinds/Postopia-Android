package com.postopia.data.repository

import com.postopia.data.model.FeedPostInfo
import com.postopia.data.model.Result
import com.postopia.data.model.SearchCommentInfo
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
                val ids = response.requireData().requireData().map { it.id }
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
                val ids = response.requireData().requireData().map { it.id }
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
    ): Flow<Result<List<SearchCommentInfo>>> = flow {
        emit(Result.Loading)
        try {
            val response = remoteDataSource.searchComment(query, page, size)
            if (response.isSuccessful()) {
                val ids = response.requireData().requireData().map { it.id }
                val commentResponse = commentRemoteDataSource.searchComments(ids)
                if (commentResponse.isSuccessful()) {
                    emit(Result.Success(commentResponse.requireData()))
                }else{
                    emit(Result.Error(Exception(commentResponse.message)))
                }
            } else {
                emit(Result.Error(Exception(response.message)))
            }
        }catch (e : Exception){
            emit(Result.Error(e))
        }
    }

}
