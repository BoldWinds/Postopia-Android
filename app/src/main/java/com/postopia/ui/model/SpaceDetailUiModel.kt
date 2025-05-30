package com.postopia.ui.model

data class SpaceDetailUiModel(
    val spaceID : Long,
    val name : String,
    val avatar : String,
    val description : String,
    val memberCount : Long,
    val postCount : Long,
    val isMember : Boolean = false,
    val createdAt : String,
){
    companion object {
        fun default(): SpaceDetailUiModel = SpaceDetailUiModel(
            spaceID = -1,
            name = "Default Space",
            description = "",
            avatar = "",
            memberCount = -1,
            postCount = -1,
            createdAt = "0",
        )
    }
}