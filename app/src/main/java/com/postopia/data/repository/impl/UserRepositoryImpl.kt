package com.postopia.data.repository.impl

import com.postopia.data.local.UserLocalDataSource
import com.postopia.data.model.Result
import com.postopia.data.model.UserDetail
import com.postopia.data.remote.UserRemoteDataSource
import com.postopia.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun getCurrentUser(): Flow<Result<UserDetail>> = flow {
        emit(Result.Loading)
        try {
            val user = UserDetail(
                userId = 1,
                avatar = "https://res.cloudinary.com/dz2tishzo/image/upload/v1744035577/2025-conference-cat_yylqqz.jpg",
                commentCount = 1001,
                credit = 114514,
                email = "whateverlbw@gmail.com",
                introduction = "Hi, this is boldwinds",
                nickname = "boldwinds",
                postCount = 11,
                showEmail = true,
                username = "lbw",
                createdAt = "1748327655",
            )
            emit(Result.Success(user))
        }catch (e : Exception){
            emit(Result.Error(e))
        }

    }
}
