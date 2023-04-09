package com.kantboot.util.common.file;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * 文件处理工具类
 * @author 方某方
 */

public class FileUtil {


    public static final HashMap<String,String> CONTENT_TYPE_MAP_BY_SUFFIX = new HashMap<>();

    static{
        CONTENT_TYPE_MAP_BY_SUFFIX.put("jpg","image/jpeg");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("jpeg","image/jpeg");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("png","image/png");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("gif","image/gif");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("bmp","image/bmp");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("ico","image/x-icon");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("svg","image/svg+xml");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("webp","image/webp");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("tiff","image/tiff");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("psd","image/vnd.adobe.photoshop");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("mp4","video/mp4");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("avi","video/x-msvideo");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("wmv","video/x-ms-wmv");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("flv","video/x-flv");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("mkv","video/x-matroska");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("mp3","audio/mpeg");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("wav","audio/x-wav");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("ogg","audio/ogg");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("wma","audio/x-ms-wma");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("flac","audio/flac");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("aac","audio/aac");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("pdf","application/pdf");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("doc","application/msword");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("xls","application/vnd.ms-excel");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("ppt","application/vnd.ms-powerpoint");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("pptx","application/vnd.openxmlformats-officedocument.presentationml.presentation");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("zip","application/zip");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("rar","application/x-rar-compressed");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("7z","application/x-7z-compressed");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("gz","application/x-gzip");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("bz2","application/x-bzip2");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("tar","application/x-tar");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("jar","application/java-archive");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("txt","text/plain");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("html","text/html");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("xml","text/xml");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("js","application/javascript");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("css","text/css");
        CONTENT_TYPE_MAP_BY_SUFFIX.put("json","application/json");
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

        // 进行了修复，原先的代码使用了lastIndexOf(".")，
        // 现在使用了更快的lastIndexOf('.')
        // 获取最后一个.的位置
        int index = fileName.lastIndexOf('.');
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
        return CONTENT_TYPE_MAP_BY_SUFFIX.get(suffix);
    }

    /**
     * 从网络Url中下载文件
     */
    public static void downloadFromUrl(String urlStr,String savePath, String fileName) throws Exception {
        FileOutputStream fos = null;
        URL url = new java.net.URL(urlStr);
        HttpURLConnection httpUrl = (java.net.HttpURLConnection) url.openConnection();
        httpUrl.connect();
        BufferedInputStream bis = new BufferedInputStream(httpUrl.getInputStream());
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            boolean mkdirs = saveDir.mkdirs();
            if (!mkdirs) {
                throw new Exception("创建文件夹失败");
            }
        }
        File file = new java.io.File(saveDir + java.io.File.separator + fileName);
        try {
            fos = new java.io.FileOutputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fos != null) {
                fos.close();
            }
        }
        int size = 1024;
        byte[] buf = new byte[size];
        int len;
        while ((len = bis.read(buf, 0, size)) != -1) {
            if (fos != null) {
                fos.write(buf, 0, len);
            }
        }
        if (fos != null) {
            fos.close();
        }
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
            int len = 8192;
            while ((bytesRead = fis.read(buffer, 0, len))!= -1)
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
