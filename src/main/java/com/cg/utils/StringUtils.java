package com.cg.utils;

public class StringUtils {

    public static String topath(String preAvatar) {
        int lastSlashIndex = preAvatar.lastIndexOf('/');

        // 获取文件名
        return preAvatar.substring(lastSlashIndex + 1);
    }
}
