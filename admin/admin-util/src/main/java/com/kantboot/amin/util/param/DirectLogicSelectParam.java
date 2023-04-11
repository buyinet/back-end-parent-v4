package com.kantboot.amin.util.param;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询参数
 * 面向查询的直接逻辑参数
 * @author 方某方
 */
@Data
public class DirectLogicSelectParam<T> {

    /**
     * 代表 =
     */
    private T eq;

    /**
     * 代表 like 查询
     */
    private T like;

    /**
     * 代表 模糊 查询
     */
    private T vague;

    /**
     * 代表 >
     */
    private T gt;

    /**
     * 代表 <
     */
    private T lt;

    /**
     * 代表 >=
     */
    private T ge;

    /**
     * 代表 <=
     */
    private T le;


    /**
     * 代表 =
     */
    private List<T> eqList = new ArrayList<>();


    /**
     * 代表 like 查询
     */
    private List<T> likeList = new ArrayList<>();

    /**
     * 代表 模糊 查询
     */
    private List<T> vagueList = new ArrayList<>();

    /**
     * 代表 >
     */
    private List<T> gtList = new ArrayList<>();

    /**
     * 代表 <
     */
    private List<T> ltList = new ArrayList<>();

    /**
     * 代表 >=
     */
    private List<T> geList = new ArrayList<>();


    /**
     * 代表 <=
     */
    private List<T> leList = new ArrayList<>();

}
