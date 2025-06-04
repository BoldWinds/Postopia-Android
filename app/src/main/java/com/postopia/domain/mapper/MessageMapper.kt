package com.postopia.domain.mapper

import com.postopia.data.model.Message
import com.postopia.ui.model.MessageCardUiModel
import com.postopia.ui.navigation.Screen

object MessageMapper {
    fun Message.toUiModel(): MessageCardUiModel {
        val (parsedContent, route) = parseMessageContent(content)
        return MessageCardUiModel(
            id = id,
            content = parsedContent,
            createdAt = createdAt,
            isRead = isRead,
            route = route
        )
    }

    private fun parseMessageContent(content: String): Pair<String, String> {
        var parsedContent = content
        var route = ""

        // 处理用户匹配: postopia-user{%d;@%s}
        val userPattern = "postopia-user\\{(\\d+);@([^}]+)\\}".toRegex()
        parsedContent = parsedContent.replace(userPattern) { matchResult ->
            val username = matchResult.groupValues[2]
            "u/$username"
        }

        // 处理空间匹配: postopia-space{%d;%s}
        val spacePattern = "postopia-space\\{(\\d+);([^}]+)\\}".toRegex()
        parsedContent = parsedContent.replace(spacePattern) { matchResult ->
            val spaceId = matchResult.groupValues[1].toLong()
            val spaceName = matchResult.groupValues[2]

            // 设置跳转路由
            if (route.isEmpty()) {
                route = Screen.SpaceDetail.createRoute(spaceId)
            }

            spaceName
        }

        // 处理帖子匹配: postopia-post{%d;%d;%s}
        val postPattern = "postopia-post\\{(\\d+);(\\d+);([^}]+)\\}".toRegex()
        parsedContent = parsedContent.replace(postPattern) { matchResult ->
            val spaceId = matchResult.groupValues[1].toLong()
            val postId = matchResult.groupValues[2].toLong()

            // 设置跳转路由
            if (route.isEmpty()) {
                route = Screen.PostDetail.createRoute(spaceId, postId)
            }

            ""
        }

        // 处理评论匹配: postopia-comment{%d;%d;%d;%s}
        val commentPattern = "postopia-comment\\{(\\d+);(\\d+);(\\d+);([^}]+)\\}".toRegex()
        parsedContent = parsedContent.replace(commentPattern) { matchResult ->
            val spaceId = matchResult.groupValues[1].toLong()
            val postId = matchResult.groupValues[2].toLong()

            // 设置跳转路由
            if (route.isEmpty()) {
                route = Screen.PostDetail.createRoute(spaceId, postId)
            }

            ""
        }

        return Pair(parsedContent, route)
    }
}