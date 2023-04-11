package com.kantboot.admin.util.old.nanshouxiangku.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor
public class CommonParam<T> implements Serializable {

    private OperatorEntity<T> or = new OperatorEntity<>();
    private OperatorEntity<T> and = new OperatorEntity<>();
    private T entity;
    private List<String> notNull=new ArrayList<>();
    private List<String> isNull=new ArrayList<>();
}
