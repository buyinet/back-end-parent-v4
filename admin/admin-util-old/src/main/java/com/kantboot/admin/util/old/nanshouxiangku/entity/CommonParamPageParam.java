package com.kantboot.admin.util.old.nanshouxiangku.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 分页查询参数接收工具
 * 用于分页的工具类
 * @param <T>
 */
@Data
@Accessors(chain = true)
public class CommonParamPageParam<T> implements Serializable {
    private int pageNumber;

    private int pageSize;

    private CommonParam<T> data;

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

    public CommonParamPageParam<T> addDataAndEq(T entity){
        if(this.getData()==null){
            this.setData(new CommonParam<>());
        }
        if(this.getData().getAnd()==null){
            this.getData().setAnd(new OperatorEntity<>());
        }
        if(this.getData().getAnd().getEq()==null){
            this.getData().getAnd().setEq(new ArrayList<>());
        }
        this.getData().getAnd().getEq().add(entity);
        return this;
    }

    public CommonParamPageParam<T> addDataAndGe(T entity) {
        if(this.getData()==null){
            this.setData(new CommonParam<>());
        }
        if(this.getData().getAnd()==null){
            this.getData().setAnd(new OperatorEntity<>());
        }
        if(this.getData().getAnd().getGe()==null){
            this.getData().getAnd().setGe(new ArrayList<>());
        }
        this.getData().getAnd().getGe().add(entity);
        return this;
    }

    public CommonParamPageParam<T> addDataAndLe(T entity) {
        if(this.getData()==null){
            this.setData(new CommonParam<>());
        }
        if(this.getData().getAnd()==null){
            this.getData().setAnd(new OperatorEntity<>());
        }
        if(this.getData().getAnd().getLe()==null){
            this.getData().getAnd().setLe(new ArrayList<>());
        }
        this.getData().getAnd().getLe().add(entity);
        return this;
    }
}
