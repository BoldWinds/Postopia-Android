package com.postopia.data.repository.impl

import com.postopia.data.model.Result
import com.postopia.data.model.SpaceInfo
import com.postopia.data.model.SpacePart
import com.postopia.data.remote.SpaceRemoteDataSource
import com.postopia.data.repository.SpaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SpaceRepositoryImpl @Inject constructor(
    private val remoteDataSource: SpaceRemoteDataSource
) : SpaceRepository{
    override suspend fun getPopularSpaces(): Flow<Result<List<SpaceInfo>>> = flow {
        emit(Result.Loading)
        try{
            // 创建模拟数据
            val mockData = listOf(
                SpaceInfo(
                    space = SpacePart(
                        id = 3,
                        name = "test",
                        avatar = "https://res.cloudinary.com/dz2tishzo/image/upload/v1747922292/6510615555426900571/2.jpeg.jpg",
                        description = "test",
                        createdAt = "2025-05-22T15:44:34.081141Z",
                        postCount = 4,
                        memberCount = 1
                    ),
                    isMember = true
                ),
                SpaceInfo(
                    space = SpacePart(
                        id = 1,
                        name = "test_space",
                        avatar = "old avatar",
                        description = "updated",
                        createdAt = "2025-05-22T13:58:12.644427Z",
                        postCount = -2,
                        memberCount = 1
                    ),
                    isMember = false
                ),
                SpaceInfo(
                    space = SpacePart(
                        id = 4,
                        name = "sample_space",
                        avatar = "https://res.cloudinary.com/dz2tishzo/image/upload/v1744035577/2025-conference-cat_yylqqz.jpg",
                        description = "test",
                        createdAt = "2025-05-23T17:39:54.370461Z",
                        postCount = 0,
                        memberCount = 0
                    ),
                    isMember = true
                )
            )
            emit(Result.Success(mockData))
        } catch (e : Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun getUserSpaces(): Flow<Result<List<SpacePart>>> = flow {
        emit(Result.Loading)
        try{
            // 创建模拟数据
            val mockData = listOf(
                SpacePart(
                    id = 3,
                    name = "test",
                    avatar = "https://res.cloudinary.com/dz2tishzo/image/upload/v1747922292/6510615555426900571/2.jpeg.jpg",
                    description = "test",
                    createdAt = "2025-05-22T15:44:34.081141Z",
                    postCount = 4,
                    memberCount = 1
                ),
                SpacePart(
                    id = 1,
                    name = "test_space",
                    avatar = "old avatar",
                    description = "updated",
                    createdAt = "2025-05-22T13:58:12.644427Z",
                    postCount = -2,
                    memberCount = 1
                )
            )
            emit(Result.Success(mockData))
        } catch (e : Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun getSpace(spaceID: Long): Flow<Result<SpaceInfo>> = flow {
        emit(Result.Loading)
        try{
            // 创建模拟数据
            val mockData = SpaceInfo(
                space = SpacePart(
                    id = 3,
                    name = "test",
                    avatar = "https://res.cloudinary.com/dz2tishzo/image/upload/v1747922292/6510615555426900571/2.jpeg.jpg",
                    description = "this is a discription",
                    createdAt = "2025-05-22T15:44:34.081141Z",
                    postCount = 4,
                    memberCount = 1
                ),
                isMember = true
            )
            emit(Result.Success(mockData))
        } catch (e : Exception) {
            emit(Result.Error(e))
        }
    }
}

