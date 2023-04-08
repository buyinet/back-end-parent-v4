package com.kantboot.util.common.file;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * 文件处理工具类
 * @author 方某方
 */

public class FileUtil {
    /**
     * SIZE
     */
    private final int size = 1024;


    public static final HashMap<String,String> contentTypeMapBySuffix = new HashMap<>();

    static{
        contentTypeMapBySuffix.put("jpg","image/jpeg");
        contentTypeMapBySuffix.put("jpeg","image/jpeg");
        contentTypeMapBySuffix.put("png","image/png");
        contentTypeMapBySuffix.put("gif","image/gif");
        contentTypeMapBySuffix.put("bmp","image/bmp");
        contentTypeMapBySuffix.put("ico","image/x-icon");
        contentTypeMapBySuffix.put("svg","image/svg+xml");
        contentTypeMapBySuffix.put("webp","image/webp");
        contentTypeMapBySuffix.put("tiff","image/tiff");
        contentTypeMapBySuffix.put("psd","image/vnd.adobe.photoshop");
        contentTypeMapBySuffix.put("mp4","video/mp4");
        contentTypeMapBySuffix.put("avi","video/x-msvideo");
        contentTypeMapBySuffix.put("wmv","video/x-ms-wmv");
        contentTypeMapBySuffix.put("flv","video/x-flv");
        contentTypeMapBySuffix.put("mkv","video/x-matroska");
        contentTypeMapBySuffix.put("mp3","audio/mpeg");
        contentTypeMapBySuffix.put("wav","audio/x-wav");
        contentTypeMapBySuffix.put("ogg","audio/ogg");
        contentTypeMapBySuffix.put("wma","audio/x-ms-wma");
        contentTypeMapBySuffix.put("flac","audio/flac");
        contentTypeMapBySuffix.put("aac","audio/aac");
        contentTypeMapBySuffix.put("pdf","application/pdf");
        contentTypeMapBySuffix.put("doc","application/msword");
        contentTypeMapBySuffix.put("docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        contentTypeMapBySuffix.put("xls","application/vnd.ms-excel");
        contentTypeMapBySuffix.put("xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        contentTypeMapBySuffix.put("ppt","application/vnd.ms-powerpoint");
        contentTypeMapBySuffix.put("pptx","application/vnd.openxmlformats-officedocument.presentationml.presentation");
        contentTypeMapBySuffix.put("zip","application/zip");
        contentTypeMapBySuffix.put("rar","application/x-rar-compressed");
        contentTypeMapBySuffix.put("7z","application/x-7z-compressed");
        contentTypeMapBySuffix.put("gz","application/x-gzip");
        contentTypeMapBySuffix.put("bz2","application/x-bzip2");
        contentTypeMapBySuffix.put("tar","application/x-tar");
        contentTypeMapBySuffix.put("jar","application/java-archive");
        contentTypeMapBySuffix.put("txt","text/plain");
        contentTypeMapBySuffix.put("html","text/html");
        contentTypeMapBySuffix.put("xml","text/xml");
        contentTypeMapBySuffix.put("js","application/javascript");
        contentTypeMapBySuffix.put("css","text/css");
        contentTypeMapBySuffix.put("json","application/json");
    }

    /**
     * 获取文件后缀
     * @param fileName 文件名
     * @return 后缀
     */
    public static String getSuffix(String fileName) {
        if (fileName == null) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return null;
        }
        return fileName.substring(index + 1);
    }

    /**
     * 获取文件类型
     * @param fileName 文件名
     *                 例如：1.jpg
     *                 返回：image/jpeg
     * @return 文件类型
     */
    public static String getContentType(String fileName) {
        String suffix = getSuffix(fileName);
        return contentTypeMapBySuffix.get(suffix);
    }

    /**
     * 从网络Url中下载文件
     */
    public static void downloadFromUrl(String urlStr,String savePath, String fileName) throws Exception {
        java.io.BufferedInputStream bis = null;
        java.io.FileOutputStream fos = null;
        java.net.HttpURLConnection httpUrl = null;
        java.net.URL url = new java.net.URL(urlStr);
        httpUrl = (java.net.HttpURLConnection) url.openConnection();
        httpUrl.connect();
        bis = new java.io.BufferedInputStream(httpUrl.getInputStream());
        java.io.File saveDir = new java.io.File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        java.io.File file = new java.io.File(saveDir + java.io.File.separator + fileName);
        fos = new java.io.FileOutputStream(file);
        int size = 1024;
        byte[] buf = new byte[size];
        int len;
        while ((len = bis.read(buf, 0, size)) != -1) {
            fos.write(buf, 0, len);
        }
        fos.close();
        bis.close();
        httpUrl.disconnect();
    }

    /**
     * 获取文件的字节流
     * @param filePath 文件地址
     * @param fileName 文件名
     * @return 字节流
     */
    public static byte[] getByte(String filePath, String fileName){
        String fieldName = "file";
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem(fieldName, "text/plain", false,fileName);
        File newfile = new File(filePath+"/"+fileName);
        int bytesRead;
        byte[] buffer = new byte[8192];
        try (FileInputStream fis = new FileInputStream(newfile);
             OutputStream os = item.getOutputStream()) {
            while ((bytesRead = fis.read(buffer, 0, 8192))!= -1)
            {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item.get();
    }

    /**
     * 获取文件的MD5值
     * @param filePath 文件地址
     *                 例如：/test
     *                      D:/test
     * @param fileName 文件名
     *                 例如：1.jpg
     * @return MD5值
     */
    public static String getMd5(String filePath, String fileName){
        return DigestUtils.md5DigestAsHex(getByte(filePath, fileName));
    }

}
