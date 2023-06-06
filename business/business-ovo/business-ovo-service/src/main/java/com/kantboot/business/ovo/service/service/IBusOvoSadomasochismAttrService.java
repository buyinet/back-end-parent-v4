package com.kantboot.business.ovo.service.service;

import com.kantboot.business.ovo.module.entity.BusOvoSadomasochismAttr;

import java.util.List;
import java.util.Map;

/**
 * Ovo性虐属性表服务接口
 * @author 方某某
 */
public interface IBusOvoSadomasochismAttrService {


    /**
     * 获取所有的性虐属性
     * @return 性虐属性
     */
    List<BusOvoSadomasochismAttr> getList();

    /**
     * 获取字典
     * @return 字典
     */
    Map<String,String> getMap();

}
