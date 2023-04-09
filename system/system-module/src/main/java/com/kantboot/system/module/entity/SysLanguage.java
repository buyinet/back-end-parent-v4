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
 * 语言实体类
 * 用于多语言，用来存储语言的编码和名称
 * 例如：zh_CN、en_US
 * @author 方某方
 */
@Table(name = "sys_language")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class SysLanguage  {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 语言编码
     */
    @Column(name = "code", length = 10)
    private String code;

    /**
     * 语言名称，例如：中文、英文
     * 这里为了方便管理，用各自的语言存储各自的语言翻译
     * 例：中文(简体)，存储为：中文(简体)
     *    英语(美国)，存储为：English (United States)
     * 在勾选时，会根据当前语言显示对应的语言名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 描述
     * 这个字段，是为了开发阶段和管理阶段方便查看
     * 基本上用中文存储
     * 例如：中文(简体)、中文(繁体)
     */
    @Column(name = "description")
    private String description;

    /**
     * 是否支持
     * 用于勾选时，是否支持该语言
     * 如果不支持该语言，那么就不会显示在勾选框中
     */
    @Column(name = "is_support")
    private Boolean support;

    /**
     * 百度翻译对应的语言编码
     * 因为百度翻译的语言编码和国际的语言编码不一样，所以需要单独存储
     * 例如：
     *      中文(简体)在国际编码中是zh_CN，但是在百度翻译中是zh
     *      中文(繁体)在国际编码中是zh_TW，但是在百度翻译中是cht
     * 在开发过程中的中文(香港)采用的应该是繁体，还是粤语的形式，所以在使用批量翻译时，要做特殊处理
     * 如何特殊处理中文(香港)？
     *      首先将中文(简体)通过百度翻译中的编码yue(对应着粤语)，翻译成粤语
     *      然后将粤语翻译成中文(繁体)
     */
    @Column(name = "baidu_translate_code", length = 10)
    private String baiduTranslateCode;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 最后一次修改时间
     */
    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;
}
