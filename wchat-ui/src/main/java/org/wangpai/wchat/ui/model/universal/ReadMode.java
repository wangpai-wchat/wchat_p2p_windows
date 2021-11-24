package org.wangpai.wchat.ui.model.universal;

/**
 * @since 2022-1-16
 */
public enum ReadMode {
    RECURSIVE, // 遍历目录下所有有文件后缀的文件

    ONE_LEVEL_SUFFIX_FILE_ONLY, // 递归遍历目录下的所有文件与文件夹

    ONE_LEVEL, // 遍历指定目录下的文件与文件夹，但不把文件夹当做文件处理，不进行深层遍历
}
