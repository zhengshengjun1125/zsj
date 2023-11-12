package com.zsj.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/11/12.
 */
public class FileGlobalHelper {

    private static Map<String, String> fileTypeMap;

    static {
        fileTypeMap = new HashMap<>();
        // 视频文件类型
        fileTypeMap.put("mp4", "视频");
        fileTypeMap.put("mov", "视频");
        fileTypeMap.put("avi", "视频");
        fileTypeMap.put("mkv", "视频");
        fileTypeMap.put("flv", "视频");
        fileTypeMap.put("wmv", "视频");
        fileTypeMap.put("rmvb", "视频");
        fileTypeMap.put("mpeg", "视频");
        fileTypeMap.put("3gp", "视频");
        fileTypeMap.put("webm", "视频");

        // 图片文件类型
        fileTypeMap.put("jpg", "图片");
        fileTypeMap.put("jpeg", "图片");
        fileTypeMap.put("png", "图片");
        fileTypeMap.put("gif", "图片");
        fileTypeMap.put("bmp", "图片");
        fileTypeMap.put("tiff", "图片");
        fileTypeMap.put("ico", "图片");

        // 音乐文件类型
        fileTypeMap.put("mp3", "音乐");
        fileTypeMap.put("wav", "音乐");
        fileTypeMap.put("flac", "音乐");
        fileTypeMap.put("aac", "音乐");
        fileTypeMap.put("wma", "音乐");
        fileTypeMap.put("ogg", "音乐");
        fileTypeMap.put("m4a", "音乐");
    }


    public static Map<String, String> fileTypeMap() {
        return fileTypeMap;
    }

    public static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

}
