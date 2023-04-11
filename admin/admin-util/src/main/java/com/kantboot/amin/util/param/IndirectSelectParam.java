package com.kantboot.amin.util.param;

import lombok.Data;


/**
 * 间接查询的参数
 * @author 方某方
 */
@Data
public class IndirectSelectParam<T> {

    /**
     * 代表 and 条件查询
     */
    private DirectSelectParam<T> and;

    /**
     * 代表 or 条件查询
     */
    private DirectSelectParam<T> or;


}
