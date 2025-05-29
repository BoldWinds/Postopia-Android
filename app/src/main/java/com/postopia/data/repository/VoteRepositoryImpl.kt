package com.postopia.data.repository

import com.postopia.data.remote.VoteRemoteDataSource
import com.postopia.domain.repository.VoteRepository
import javax.inject.Inject

class VoteRepositoryImpl @Inject constructor(
    private val remoteDataSource: VoteRemoteDataSource
) : VoteRepository {

}