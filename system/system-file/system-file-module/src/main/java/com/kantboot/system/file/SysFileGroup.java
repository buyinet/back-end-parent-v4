package com.kantboot.system.file;

import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * 文件组管理实体类
 * 用于管理文件组的上传、下载、路径等
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "sys_file_group")
public class SysFileGroup {

    /**
     * 文件组编码，用于文件组下载、删除、访问等
     * 如：头像、文章图片等
     */
    @Id
    @Column(name = "code",length = 64)
    private String code;

    /**
     * 文件组名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 文件组路径
     */
    @Column(name = "path")
    private String path;


    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;
}
