package com.kantboot.util.common.file;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class FileUtilTest {

    /**
     * 获取文件的后缀名
     */
    @Test
    void getSuffix() {
        String suffix = FileUtil.getSuffix("/file/test.txt");
        // 返回结果：/file/test.txt = txt
        System.out.println("/file/test.txt = " + suffix);

        suffix = FileUtil.getSuffix("/file/test");
        // 返回结果：/file/test = null
        System.out.println("/file/test = " + suffix);

        suffix = FileUtil.getSuffix("test.png");
        // 返回结果：test.png = png
        System.out.println("test.png = " + suffix);
    }

    /**
     * 获取文件的ContentType
     */
    @Test
    void getContentType() {
        String contentType = FileUtil.getContentType("/file/test.txt");
        // 返回结果：/file/test.txt = text/plain
        System.out.println("/file/test.txt = " + contentType);

        contentType = FileUtil.getContentType("/file/test");
        // 返回结果：/file/test = null
        System.out.println("/file/test = " + contentType);

        contentType = FileUtil.getContentType("test.png");
        // 返回结果：test.png = image/png
        System.out.println("test.png = " + contentType);
    }

    /**
     * 从网络上下载文件
     */
    @SneakyThrows
    @Test
    void downloadFile() {
        String url = "https://www.baidu.com/img/bd_logo1.png";
        // 下载到D盘的test目录下，文件名为baidu.png，重复下载会覆盖
        FileUtil.downloadFromUrl(url,"D:\\test","baidu.png");
    }

}
