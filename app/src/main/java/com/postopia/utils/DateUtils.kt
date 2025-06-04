package com.postopia.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs

object DateUtils {

    /**
     * 将ISO 8601格式的字符串转换为指定格式的日期字符串
     *
     * @param dateString ISO格式的日期字符串，如"2023-05-10T14:30:00"
     * @param outputPattern 输出格式，默认为"MMM d, yyyy"
     * @param inputPattern 输入格式，默认为"yyyy-MM-dd'T'HH:mm:ss"
     * @return 格式化后的日期字符串
     */
    fun formatDate(
        dateString: String,
        outputPattern: String = "yyyy-MM-dd HH:mm",
        inputPattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
    ): String {
        return try {
            val inputFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
            inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(dateString) ?: return dateString

            val now = System.currentTimeMillis()
            val diff = now - date.time

            // 根据时间差返回相对时间描述
            return when {
                diff < 60 * 1000 -> "刚刚" // 小于1分钟
                diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}分钟前" // 小于1小时
                diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)}小时前" // 小于24小时
                else -> {
                    // 超过24小时显示具体日期时间
                    val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
                    outputFormat.format(date)
                }
            }
        } catch (e: ParseException) {
            // 尝试使用不同的格式解析
            try {
                val alternativePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'"
                val altFormat = SimpleDateFormat(alternativePattern, Locale.getDefault())
                altFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
                val date = altFormat.parse(dateString) ?: return dateString
                val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
                return outputFormat.format(date)
            } catch (e2: Exception) {
                dateString
            }
        } catch (e: Exception) {
            dateString
        }
    }

    fun absoluteTime(
        dateString: String,
        inputPattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
    ): String {
        return try {
            val inputFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
            inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(dateString) ?: return dateString

            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            outputFormat.format(date)
        } catch (e: Exception) {
            dateString
        }
    }

    fun formatRelativeTime(
        dateString: String,
        inputPattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
    ): String {
        return try {
            val inputFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
            inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(dateString) ?: return dateString

            val now = System.currentTimeMillis()
            val diff = date.time - now // 正值表示未来，负值表示过去
            val absDiff = abs(diff)

            // 分钟和小时
            val minutes = absDiff / (60 * 1000) % 60
            val hours = absDiff / (60 * 60 * 1000)

            return when {
                // 刚刚/即将
                absDiff < 60 * 1000 -> if (diff < 0) "刚刚" else "即将"

                // 仅分钟
                hours == 0L -> {
                    if (diff < 0) "${minutes}分钟前" else "${minutes}分钟后"
                }

                // 小时和分钟
                else -> {
                    val timeStr = if (minutes > 0) "${hours}小时${minutes}分钟" else "${hours}小时"
                    if (diff < 0) "${timeStr}前" else "${timeStr}后"
                }
            }
        } catch (e: Exception) {
            dateString
        }
    }

}