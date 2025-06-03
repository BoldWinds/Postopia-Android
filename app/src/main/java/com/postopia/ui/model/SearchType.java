package com.postopia.ui.model;

public enum SearchType {
    POST,
    SPACE,
    USER,
    COMMENT;

    public static SearchType fromString(String text) {
        for (SearchType type : SearchType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的搜索类型: " + text);
    }
}
