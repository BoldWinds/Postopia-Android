package com.postopia.data.repository

import com.postopia.data.model.Result
import com.postopia.data.model.SpaceInfo
import com.postopia.data.model.SpacePart
import kotlinx.coroutines.flow.Flow

interface SpaceRepository {

    suspend fun getPopularSpaces(page : Int = 0): Flow<Result<List<SpaceInfo>>>

    suspend fun getUserSpaces(page : Int = 0) : Flow<Result<List<SpacePart>>>

    suspend fun getSpace(spaceID : Long): Flow<Result<SpaceInfo>>

    suspend fun joinSpace(spaceID: Long): Flow<Result<Unit>>

    suspend fun leaveSpace(spaceID: Long): Flow<Result<Unit>>

}