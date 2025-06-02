package com.postopia.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.postopia.data.model.OpinionStatus
import com.postopia.data.model.RecursiveCommentInfo
import com.postopia.data.model.Result
import com.postopia.domain.mapper.CommentMapper.toUiModel
import com.postopia.domain.mapper.PostMapper.toUiModel
import com.postopia.domain.mapper.VoteMapper.toUiModel
import com.postopia.domain.repository.CommentRepository
import com.postopia.domain.repository.OpinionRepository
import com.postopia.domain.repository.PostRepository
import com.postopia.ui.model.CommentTreeNodeUiModel
import com.postopia.ui.model.PostDetailUiModel
import com.postopia.ui.model.VoteDialogUiModel
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
    val hasMoreComments : Boolean = true,
    val vote : VoteDialogUiModel? = null,
    val commentVotes : Map<Long, VoteDialogUiModel> = emptyMap<Long, VoteDialogUiModel>(),
    val replyToComment: CommentTreeNodeUiModel? = null,  // 当前回复的评论ID，null表示直接回复帖子
    val isReplyBoxVisible: Boolean = false,  // 控制回复框是否可见
)

sealed class PostDetailEvent {
    object SnackbarMessageShown : PostDetailEvent()
    object LoadComments : PostDetailEvent()
    data class LoadPostDetail(val postId: Long, val spaceId: Long, val spaceNam: String) : PostDetailEvent()
    data class UpdatePostOpinion(val postId: Long, val spaceId: Long, val isPositive : Boolean) : PostDetailEvent()
    data class CancelPostOpinion(val postId: Long,val isPositive : Boolean) : PostDetailEvent()
    data class UpdateCommentOpinion(val commentId: Long, val spaceId: Long ,val isPositive: Boolean) : PostDetailEvent()
    data class CancelCommentOpinion(val commentId: Long, val isPositive: Boolean) : PostDetailEvent()
    data class VoteOpinion(val voteId: Long, val isPositive: Boolean) : PostDetailEvent()
    data class CommentVoteOpinion(val commentId: Long, val voteId: Long, val isPositive: Boolean) : PostDetailEvent()

    // 新增回复相关事件
    data class ShowReplyBox(val comment: CommentTreeNodeUiModel?) : PostDetailEvent()
    object HideReplyBox : PostDetailEvent()
    data class SendReply(val content: String) : PostDetailEvent()
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
                _uiState.update { it.copy(commentsPage = 0) }
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
            is PostDetailEvent.VoteOpinion -> {
                voteOpinion(event.voteId, event.isPositive)
            }
            is PostDetailEvent.CommentVoteOpinion -> {
                voteOpinion(event.voteId, event.isPositive, event.commentId)
            }
            is PostDetailEvent.ShowReplyBox -> {
                _uiState.update { it.copy(replyToComment = event.comment, isReplyBoxVisible = true) }
            }
            is PostDetailEvent.HideReplyBox -> {
                _uiState.update { it.copy(replyToComment = null, isReplyBoxVisible = false) }
            }
            is PostDetailEvent.SendReply -> {
                sendReply(event.content)
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
                        _uiState.update {
                            it.copy(
                                postDetail = result.data.toUiModel(spaceId, spaceName),
                                vote = result.data.vote?.toUiModel(),
                                isLoading = false)
                        }
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
                        // 从评论树中递归提取所有投票信息
                        val newVotes = mutableMapOf<Long, VoteDialogUiModel>()

                        // 递归函数用于遍历评论树并提取投票信息
                        fun extractVotes(comments: List<RecursiveCommentInfo>) {
                            for (commentInfo in comments) {
                                // 如果评论有投票信息，则添加到map中
                                if (commentInfo.comment.vote != null) {
                                    newVotes[commentInfo.comment.comment.id] = commentInfo.comment.vote.toUiModel()
                                }
                                // 递归处理子评论
                                if (commentInfo.children.isNotEmpty()) {
                                    extractVotes(commentInfo.children)
                                }
                            }
                        }

                        extractVotes(result.data)

                        _uiState.update { currentState ->
                            val updatedComments = currentState.comments + result.data.map { it.toUiModel() }
                            // 合并现有和新的投票信息
                            val updatedVotes = currentState.commentVotes + newVotes

                            currentState.copy(
                                comments = updatedComments,
                                isLoadingComments = false,
                                hasMoreComments = result.data.isNotEmpty(),
                                commentsPage = page + 1,
                                commentVotes = updatedVotes
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

    fun voteOpinion(voteId: Long, isPositive: Boolean, commentID: Long? = null) {
        val vote = if (commentID != null)   _uiState.value.commentVotes[commentID] else _uiState.value.vote
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
                                if (commentID != null) {
                                    // 更新评论投票状态
                                    val updatedVote = vote.copy(
                                        positiveCount = newPositiveCount,
                                        negativeCount = newNegativeCount,
                                        opinion = if (isPositive) OpinionStatus.POSITIVE else OpinionStatus.NEGATIVE
                                    )
                                    val updatedCommentVotes = currentState.commentVotes.toMutableMap().apply {
                                        put(commentID, updatedVote)
                                    }
                                    currentState.copy(commentVotes = updatedCommentVotes)
                                } else {
                                    // 更新帖子投票状态
                                    val updatedVote = vote.copy(
                                        positiveCount = newPositiveCount,
                                        negativeCount = newNegativeCount,
                                        opinion = if (isPositive) OpinionStatus.POSITIVE else OpinionStatus.NEGATIVE
                                    )
                                    currentState.copy(vote = updatedVote)
                                }
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

    fun sendReply(content: String) {
        val parenID = _uiState.value.replyToComment?.id
        viewModelScope.launch {
            commentRepository.replyComment(
                postId = _uiState.value.postDetail.postID,
                spaceId = _uiState.value.postDetail.spaceID,
                userId = _uiState.value.postDetail.userID,
                content = content,
                parentId = parenID,
            ).collect { result ->
                when (result) {
                    is Result.Loading -> { }
                    is Result.Success -> {
                        _uiState.update { it.copy(isReplyBoxVisible = false, commentsPage = 0, comments = emptyList()) }
                        // reload
                        loadComments(uiState.value.postDetail.postID)
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(snackbarMessage = result.exception.message) }
                    }
                }
            }
        }
    }
}
