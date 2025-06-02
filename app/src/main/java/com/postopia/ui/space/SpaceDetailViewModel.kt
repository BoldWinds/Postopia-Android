package com.postopia.ui.space

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.postopia.data.model.OpinionStatus
import com.postopia.data.model.Result
import com.postopia.domain.mapper.PostMapper.toPostCardInfo
import com.postopia.domain.mapper.SpaceMapper.toUiModel
import com.postopia.domain.mapper.VoteMapper.toUiModel
import com.postopia.domain.repository.OpinionRepository
import com.postopia.domain.repository.PostRepository
import com.postopia.domain.repository.SpaceRepository
import com.postopia.domain.repository.VoteRepository
import com.postopia.ui.model.PostCardInfo
import com.postopia.ui.model.SpaceDetailUiModel
import com.postopia.ui.model.VoteDialogUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SpaceDetailUiState(
    val isLoading : Boolean = true,
    val snackbarMessage: String? = null,
    val spaceInfo : SpaceDetailUiModel = SpaceDetailUiModel.default(),
    val spacePosts : List<PostCardInfo> = emptyList<PostCardInfo>(),
    val isLoadingMore : Boolean = false,
    val hasMore : Boolean = true,
    val page : Int = 0,
    val votes : List<VoteDialogUiModel> = emptyList<VoteDialogUiModel>()
)

sealed class SpaceDetailEvent {
    object SnackbarMessageShown : SpaceDetailEvent()
    data class LoadSpaceDetail(val spaceId: Long) : SpaceDetailEvent()
    data class JoinOrLeave(val spaceId: Long, val join : Boolean) : SpaceDetailEvent()
    object LoadMorePosts : SpaceDetailEvent()
    data class VoteOpinion(val voteId: Long, val isPositive: Boolean) : SpaceDetailEvent()
}

@HiltViewModel
class SpaceDetailViewModel @Inject constructor(
    private val spaceRepository: SpaceRepository,
    private val postRepository : PostRepository,
    private val voteRepository : VoteRepository,
    private val opinionRepository : OpinionRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SpaceDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun handleEvent(event: SpaceDetailEvent){
        when(event){
            is SpaceDetailEvent.SnackbarMessageShown -> {
                _uiState.update { it.copy(snackbarMessage = null) }
            }
            is SpaceDetailEvent.LoadSpaceDetail -> {
                loadSpaceDetail(event.spaceId)
            }
            is SpaceDetailEvent.JoinOrLeave -> {
                joinOrLeave(event.spaceId, event.join)
            }
            is SpaceDetailEvent.LoadMorePosts -> {
                loadPosts()
            }
            is SpaceDetailEvent.VoteOpinion -> {
                voteOpinion(event.voteId, event.isPositive)
            }
        }
    }

    fun loadSpaceDetail(spaceId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val spaceInfoJob = launch {
                spaceRepository.getSpace(spaceId).collect { result ->
                    when (result) {
                        is Result.Loading -> { }
                        is Result.Success -> {
                            _uiState.update { it.copy(spaceInfo = result.data.toUiModel()) }
                        }
                        is Result.Error -> {
                            _uiState.update { it.copy(snackbarMessage = result.message) }
                        }
                    }
                }
            }

            // 先获取空间信息，以便后续使用空间名称
            spaceInfoJob.join()

            val spacePostsJob = launch {
                postRepository.getSpacePosts(spaceId = spaceId, page = 0).collect { result ->
                    when (result) {
                        is Result.Loading -> { }
                        is Result.Success -> {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    spacePosts = result.data.map { it.toPostCardInfo(spaceId, currentState.spaceInfo.name) },
                                    page = 0,
                                    hasMore = result.data.isNotEmpty()
                                )
                            }
                        }
                        is Result.Error -> {
                            _uiState.update { it.copy(snackbarMessage = result.message) }
                        }
                    }
                }
            }

            val spaceVotesJob = launch {
                voteRepository.getSpaceVotes(spaceId = spaceId).collect { result ->
                    when (result) {
                        is Result.Loading -> { }
                        is Result.Success -> {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    votes = result.data.map { it.toUiModel() },
                                )
                            }
                        }
                        is Result.Error -> {
                            _uiState.update { it.copy(snackbarMessage = result.message) }
                        }
                    }
                }
            }

            spacePostsJob.join()
            spaceVotesJob.join()
            _uiState.update{ it.copy(isLoading = false) }
        }
    }

    fun loadPosts(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }

            postRepository.getSpacePosts(
                spaceId = _uiState.value.spaceInfo.spaceID,
                page = _uiState.value.page + 1
            ).collect { result ->
                when (result) {
                    is Result.Loading -> { }
                    is Result.Success -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                spacePosts = currentState.spacePosts + result.data.map { it.toPostCardInfo(currentState.spaceInfo.spaceID, currentState.spaceInfo.name) },
                                isLoadingMore = false,
                                hasMore = result.data.isNotEmpty(),
                                page = currentState.page + 1
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(snackbarMessage = result.message, isLoadingMore = false) }
                    }
                }
            }
        }
    }

    fun joinOrLeave(id: Long, join: Boolean) {
        viewModelScope.launch {
            val repository = if (join) {
                spaceRepository.leaveSpace(id)
            } else {
                spaceRepository.joinSpace(id)
            }

            repository.collect { result ->
                when (result) {
                    is Result.Loading -> { }
                    is Result.Success -> {
                        _uiState.update { currentState ->
                            val updatedSpaceInfo = currentState.spaceInfo.copy(
                                isMember = !join
                            )
                            currentState.copy(spaceInfo = updatedSpaceInfo)
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(snackbarMessage = result.message) }
                    }
                }
            }
        }
    }

    fun voteOpinion(voteId: Long, isPositive: Boolean) {
        val vote = _uiState.value.votes.find { it.voteID == voteId }
        if(vote == null){
            _uiState.update { it.copy(snackbarMessage = "Vote Null Error!") }
        } else {
            val (newPositiveCount, newNegativeCount) = when (vote.opinion) {
                OpinionStatus.POSITIVE -> {
                    if (isPositive) {
                        // 保持赞成
                        return
                    } else {
                        // 从赞成改为反对
                        Pair(vote.positiveCount - 1, vote.negativeCount + 1)
                    }
                }
                OpinionStatus.NEGATIVE -> {
                    if (isPositive) {
                        // 从反对改为赞成
                        Pair(vote.positiveCount + 1, vote.negativeCount - 1)
                    } else {
                        // 保持反对
                        return
                    }
                }
                OpinionStatus.NIL -> {
                    // 新增投票
                    Pair(
                        if (isPositive) vote.positiveCount + 1 else vote.positiveCount,
                        if (!isPositive) vote.negativeCount + 1 else vote.negativeCount
                    )
                }
            }
            viewModelScope.launch {
                // 这个ViewModel中只有帖子和评论投票，所以isCommon一定为false
                opinionRepository.voteOpinion(false, voteId, isPositive).collect { result->
                    when (result) {
                        is Result.Loading -> { }
                        is Result.Success -> {
                            _uiState.update { currentState ->
                                // 更新帖子投票状态
                                val updatedVotes = currentState.votes.map {
                                    if (it.voteID == voteId) {
                                        it.copy(
                                            positiveCount = newPositiveCount,
                                            negativeCount = newNegativeCount,
                                            opinion = if (isPositive) OpinionStatus.POSITIVE else OpinionStatus.NEGATIVE
                                        )
                                    } else {
                                        it
                                    }
                                }
                                currentState.copy(votes = updatedVotes)
                            }
                        }
                        is Result.Error -> {
                            _uiState.update { it.copy(snackbarMessage = result.exception.message) }
                        }
                    }
                }
            }
        }
    }
}
