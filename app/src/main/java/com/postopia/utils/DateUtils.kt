package com.postopia.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        outputPattern: String = "MMM d, yyyy",
        inputPattern: String = "yyyy-MM-dd'T'HH:mm:ss"
    ): String {
        return try {
            val inputFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
            val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
            val date = inputFormat.parse(dateString)
            date?.let { outputFormat.format(it) } ?: dateString
        } catch (e: ParseException) {
            dateString
        } catch (e: Exception) {
            dateString
        }
    }

    /**
     * 获取相对时间描述，如"3天前"、"刚刚"等
     *
     * @param dateString ISO格式的日期字符串
     * @param inputPattern 输入格式
     * @return 相对时间描述
     */
    fun getRelativeTimeSpan(
        dateString: String,
        inputPattern: String = "yyyy-MM-dd'T'HH:mm:ss"
    ): String {
        try {
            val inputFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
            val date = inputFormat.parse(dateString) ?: return dateString
            val now = System.currentTimeMillis()
            val diff = now - date.time

            // 转换为适当的时间单位
            return when {
                diff < 60 * 1000 -> "刚刚" // 小于1分钟
                diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}分钟前" // 小于1小时
                diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)}小时前" // 小于24小时
                diff < 30 * 24 * 60 * 60 * 1000L -> "${diff / (24 * 60 * 60 * 1000)}天前" // 小于30天
                else -> formatDate(dateString) // 超过30天显示具体日期
            }
        } catch (e: Exception) {
            return dateString
        }
    }

    /**
     * 将时间戳转换为指定格式的日期字符串
     *
     * @param timestamp 时间戳(毫秒)
     * @param pattern 输出格式
     * @return 格式化后的日期字符串
     */
    fun formatTimestamp(timestamp: Long, pattern: String = "yyyy-MM-dd"): String {
        return try {
            val format = SimpleDateFormat(pattern, Locale.getDefault())
            format.format(Date(timestamp))
        } catch (e: Exception) {
            timestamp.toString()
        }
    }

    /**
     * 将字符串类型的时间戳转换为指定格式的日期字符串
     *
     * @param timestampString 字符串类型的时间戳(毫秒)
     * @param pattern 输出格式
     * @return 格式化后的日期字符串
     */
    fun formatTimestampFromString(timestampString: String, pattern: String = "yyyy-MM-dd"): String {
        return try {
            val timestamp = timestampString.toLong()
            formatTimestamp(timestamp, pattern)
        } catch (e: NumberFormatException) {
            timestampString
        } catch (e: Exception) {
            timestampString
        }
    }

}