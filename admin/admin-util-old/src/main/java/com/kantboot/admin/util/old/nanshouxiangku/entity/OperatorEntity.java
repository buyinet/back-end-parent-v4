package com.kantboot.admin.util.old.nanshouxiangku.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 关于运算符的实体类
 * @param <T>
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class OperatorEntity<T> implements Serializable {

    /**
     * 代表 =
     */
    private List<T> eq = new ArrayList<>();


    /**
     * 代表 like 查询
     */
    private List<T> like = new ArrayList<>();

    /**
     * 代表 模糊 查询
     */
    private List<T> vague = new ArrayList<>();

    /**
     * 代表 >
     */
    private List<T> gt = new ArrayList<>();

    /**
     * 代表 <
     */
    private List<T> lt = new ArrayList<>();

    /**
     * 代表 >=
     */
    private List<T> ge = new ArrayList<>();


    /**
     * 代表 <=
     */
    private List<T> le = new ArrayList<>();

    T entity;

}
