package com.kantboot.system.module.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * 文件管理实体类
 * 用于管理文件的上传、下载等
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "sys_file")
public class SysFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 文件组编码，用于文件组下载、删除、访问等
     * 如：头像、文章图片等
     */
    @Column(name = "file_group_code",length = 64)
    private String groupCode;

    /**
     * 文件名
     */
    @Column(name = "file_name")
    private String name;

    /**
     * 文件路径
     */
    @Column(name = "file_path")
    private String path;

    /**
     * 文件类型
     */
    @Column(name = "file_type")
    private String type;

    /**
     * 文件ContentType
     */
    @Column(name = "file_content_type")
    private String contentType;

    /**
     * 文件大小
     */
    @Column(name = "file_size")
    private Long size;

    /**
     * 文件MD5值，通过MD5值判断文件是否重复
     */
    @Column(name = "file_md5",length = 64)
    private String md5;

    /**
     * 文件上传者id
     */
    @Column(name = "user_id_of_upload")
    private Long userIdOfUpload;

    /**
     * 上传时IP
     */
    @Column(name = "ip_of_upload")
    private String ipOfUpload;

    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;

}
