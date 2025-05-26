package com.postopia.data.repository.impl

import com.postopia.data.local.UserLocalDataSource
import com.postopia.data.remote.UserRemoteDataSource
import com.postopia.data.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) : UserRepository {
}
