package com.kantboot.admin.util.old.nanshouxiangku.util;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * 分页查询参数接收工具
 * 用于分页的工具类
 * @param <T>
 */
@Data
@Accessors(chain = true)
public class PageParam<T> implements Serializable {

    private int pageNumber;

    private int pageSize;

    private T data;

    String sortType="ASC";

    String sortField="id";

    /**
     * jpa框架的分页插件
     */
    public Pageable getPageable(){
        Sort sort = Sort.by(sortField);
        Sort sort1=
                sortType.toUpperCase().equals("ASC")?
                sort.ascending():sort.descending();
        PageRequest result = PageRequest.of(pageNumber-1, pageSize, sort1);
        return result;
    }

}
