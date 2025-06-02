package com.postopia.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.postopia.data.model.Result
import com.postopia.domain.mapper.SpaceMapper.toUiModel
import com.postopia.domain.repository.PostRepository
import com.postopia.domain.repository.SpaceRepository
import com.postopia.ui.model.SpaceDetailUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreateUiState(
    val snackbarMessage: String? = null,
    val spaces: List<SpaceDetailUiModel> = emptyList(),
    val selectedSpace: SpaceDetailUiModel? = null,
    val title: String = "",
    val spacePage: Int = 0,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true,
)

sealed class CreateEvent {
    object SnackbarMessageShown : CreateEvent()
    data class CreatePost(val content: String) : CreateEvent()
    object LoadMoreSpaces : CreateEvent()
    data class SelectSpace(val space: SpaceDetailUiModel) : CreateEvent()
    data class ModifyTitle(val title: String) : CreateEvent()
}

@HiltViewModel
class CreateViewModel  @Inject constructor(
    private val spaceRepository: SpaceRepository,
    private val postRepository: PostRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadSpaces()
    }

    fun handleEvent(event: CreateEvent) {
        when (event) {
            is CreateEvent.SnackbarMessageShown -> {
                _uiState.value = _uiState.value.copy(snackbarMessage = null)
            }
            is CreateEvent.CreatePost -> {
                post(event.content)
            }
            is CreateEvent.SelectSpace -> {
                _uiState.value = _uiState.value.copy(
                    selectedSpace = event.space,
                )
            }
            is CreateEvent.ModifyTitle -> {
                _uiState.value = _uiState.value.copy(title = event.title)
            }
            is CreateEvent.LoadMoreSpaces -> {
                loadSpaces()
            }
        }
    }

    fun loadSpaces(){
        viewModelScope.launch {
            spaceRepository.getUserSpaces(page = _uiState.value.spacePage).collect { result ->
                when (result){
                    is Result.Loading -> {
                        _uiState.value.copy(isLoadingMore = true)
                    }
                    is Result.Success -> {
                        val spaces = result.data.map { it.toUiModel() }
                        _uiState.value = _uiState.value.copy(
                            spaces = spaces,
                            isLoadingMore = false,
                            hasMore = result.data.isNotEmpty(),
                            spacePage = _uiState.value.spacePage + 1,
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            snackbarMessage = result.exception.message,
                            isLoadingMore = false
                        )
                    }
                }
            }
        }
    }

    fun post(content: String){
        if(_uiState.value.selectedSpace == null) {
            _uiState.value = _uiState.value.copy(snackbarMessage = "请选择空间")
            return
        }

        if (content.isEmpty()) {
            _uiState.value = _uiState.value.copy(snackbarMessage = "内容不能为空")
            return
        }

        val selectedSpace = _uiState.value.selectedSpace!!
        viewModelScope.launch {
            // 优先使用富文本编辑器的HTML内容，如果为空则使用普通内容
            postRepository.createPost(
                spaceId = selectedSpace.spaceID,
                spaceName = selectedSpace.name,
                subject = _uiState.value.title,
                content = content,
            ).collect { result->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            snackbarMessage = "发布成功",
                            title = "",
                            selectedSpace = null,
                        )
                        // todo quit
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            snackbarMessage = result.exception.message
                        )
                    }
                }
            }
        }
    }
}

