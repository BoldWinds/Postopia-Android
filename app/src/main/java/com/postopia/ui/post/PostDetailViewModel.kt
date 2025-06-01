package com.postopia.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.postopia.data.model.OpinionStatus
import com.postopia.data.model.Result
import com.postopia.domain.mapper.CommentMapper.toUiModel
import com.postopia.domain.mapper.PostMapper.toUiModel
import com.postopia.domain.repository.CommentRepository
import com.postopia.domain.repository.OpinionRepository
import com.postopia.domain.repository.PostRepository
import com.postopia.ui.model.CommentTreeNodeUiModel
import com.postopia.ui.model.PostDetailUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PostDetailUiState(
    val isLoading: Boolean = false,
    val snackbarMessage: String? = null,
    val postDetail: PostDetailUiModel = PostDetailUiModel.default(),
    val comments : List<CommentTreeNodeUiModel> = emptyList<CommentTreeNodeUiModel>(),
    val commentsPage : Int = 0,
    val isLoadingComments : Boolean = false,
    val hasMoreComments : Boolean = false,
)

sealed class PostDetailEvent {
    object SnackbarMessageShown : PostDetailEvent()
    object LoadComments : PostDetailEvent()
    data class LoadPostDetail(val postId: Long, val spaceId: Long, val spaceNam: String) : PostDetailEvent()
    data class UpdatePostOpinion(val postId: Long, val spaceId: Long, val isPositive : Boolean) : PostDetailEvent()
    data class CancelPostOpinion(val postId: Long,val isPositive : Boolean) : PostDetailEvent()
    data class UpdateCommentOpinion(val commentId: Long, val spaceId: Long ,val isPositive: Boolean) : PostDetailEvent()
    data class CancelCommentOpinion(val commentId: Long, val isPositive: Boolean) : PostDetailEvent()
}

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val opinionRepository: OpinionRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun handleEvent(event: PostDetailEvent) {
        when (event) {
            is PostDetailEvent.SnackbarMessageShown -> {
                _uiState.update { it.copy(snackbarMessage = null) }
            }
            is PostDetailEvent.LoadPostDetail -> {
                loadPostDetail(event.postId, event.spaceId, event.spaceNam)
                loadComments(event.postId)
            }
            is PostDetailEvent.UpdatePostOpinion -> {
                sendOpinion(event.postId, event.spaceId, event.isPositive)
            }
            is PostDetailEvent.CancelPostOpinion -> {
                cancelOpinion(event.postId, event.isPositive)
            }
            is PostDetailEvent.UpdateCommentOpinion -> {
                sendCommentOpinion(event.commentId, event.spaceId, event.isPositive)
            }
            is PostDetailEvent.CancelCommentOpinion ->{
                cancelCommentOpinion(event.commentId, event.isPositive)
            }
            is PostDetailEvent.LoadComments -> {
                loadComments(uiState.value.postDetail.postID)
            }
        }
    }

    fun loadPostDetail(postId: Long, spaceId: Long, spaceName: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            postRepository.getPostByID(postId).collect { result->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        _uiState.update { it.copy(postDetail = result.data.toUiModel(spaceId, spaceName), isLoading = false) }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false, snackbarMessage = result.exception.message) }
                    }
                }
            }
        }
    }

    fun loadComments(postId: Long) {
        _uiState.update { it.copy(isLoadingComments = true) }
        val page = uiState.value.commentsPage
        viewModelScope.launch {
            commentRepository.getPostComments(postId, page).collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        _uiState.update { currentState ->
                            val updatedComments = currentState.comments + result.data.map { it.toUiModel() }
                            currentState.copy(
                                comments = updatedComments,
                                isLoadingComments = false,
                                hasMoreComments = result.data.isNotEmpty(),
                                commentsPage = page + 1
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(
                            isLoadingComments = false,
                            snackbarMessage = result.exception.message
                        ) }
                    }
                }
            }
        }
    }

    fun sendOpinion(postId: Long, spaceId: Long, isPositive: Boolean) {
        viewModelScope.launch {
            opinionRepository.updatePostOpinion(postId, spaceId, isPositive).collect { result ->
                when (result) {
                    is Result.Loading -> { }
                    is Result.Success -> {
                        _uiState.update { currentState ->
                            val currentPostDetail = currentState.postDetail
                            val updatedPostDetail = if (isPositive) {
                                currentPostDetail.copy(
                                    positiveCount = currentPostDetail.positiveCount + 1,
                                    opinionStatus = OpinionStatus.POSITIVE
                                )
                            } else {
                                currentPostDetail.copy(
                                    negativeCount = currentPostDetail.negativeCount + 1,
                                    opinionStatus = OpinionStatus.NEGATIVE
                                )
                            }
                            currentState.copy(postDetail = updatedPostDetail)
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy( snackbarMessage = result.exception.message ) }
                    }
                }
            }
        }
    }

    fun cancelOpinion(postId: Long, isPositive: Boolean) {
        viewModelScope.launch {
            opinionRepository.cancelPostOpinion(postId, isPositive).collect { result ->
                when (result) {
                    is Result.Loading -> { }
                    is Result.Success -> {
                        _uiState.update { currentState ->
                            val currentPostDetail = currentState.postDetail
                            val updatedPostDetail = if (isPositive) {
                                currentPostDetail.copy(
                                    positiveCount = currentPostDetail.positiveCount - 1,
                                    opinionStatus = OpinionStatus.NIL
                                )
                            } else {
                                currentPostDetail.copy(
                                    negativeCount = currentPostDetail.negativeCount - 1,
                                    opinionStatus = OpinionStatus.NIL
                                )
                            }
                            currentState.copy(postDetail = updatedPostDetail)
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy( snackbarMessage = result.exception.message ) }
                    }
                }
            }
        }
    }

    fun sendCommentOpinion(commentId: Long, spaceId: Long, isPositive: Boolean) {
        viewModelScope.launch {
            opinionRepository.updateCommentOpinion(commentId, spaceId, isPositive).collect { result ->
                when (result) {
                    is Result.Loading -> { }
                    is Result.Success -> {
                        _uiState.update { currentState ->
                            val updatedComments = currentState.comments.map { comment ->
                                if (comment.id == commentId) {
                                    comment.copy(
                                        positiveCount = if (isPositive) comment.positiveCount + 1 else comment.positiveCount,
                                        negativeCount = if (!isPositive) comment.negativeCount + 1 else comment.negativeCount,
                                        opinion = if (isPositive) OpinionStatus.POSITIVE else OpinionStatus.NEGATIVE
                                    )
                                } else {
                                    comment
                                }
                            }
                            currentState.copy(comments = updatedComments)
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy( snackbarMessage = result.exception.message ) }
                    }
                }
            }
        }
    }

    fun cancelCommentOpinion(commentId: Long, isPositive: Boolean) {
        viewModelScope.launch {
            opinionRepository.cancelCommentOpinion(commentId, isPositive).collect { result ->
                when (result) {
                    is Result.Loading -> { }
                    is Result.Success -> {
                        _uiState.update { currentState ->
                            val updatedComments = currentState.comments.map { comment ->
                                if (comment.id == commentId) {
                                    comment.copy(
                                        positiveCount = if (isPositive) comment.positiveCount - 1 else comment.positiveCount,
                                        negativeCount = if (!isPositive) comment.negativeCount - 1 else comment.negativeCount,
                                        opinion = OpinionStatus.NIL
                                    )
                                } else {
                                    comment
                                }
                            }
                            currentState.copy(comments = updatedComments)
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy( snackbarMessage = result.exception.message ) }
                    }
                }
            }
        }
    }
}