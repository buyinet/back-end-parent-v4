package com.kantboot.business.ovo.service.service.impl;

import com.kantboot.business.ovo.module.entity.BusOvoSadomasochismAttr;
import com.kantboot.business.ovo.service.repository.BusOvoSadomasochismAttrRepository;
import com.kantboot.business.ovo.service.service.IBusOvoSadomasochismAttrService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Ovo性虐属性表服务接口实现类
 * @author 方某某
 */
@Service
public class BusOvoSadomasochismAttrServiceImpl implements IBusOvoSadomasochismAttrService {

    @Resource
    private BusOvoSadomasochismAttrRepository repository;

    @Override
    public List<BusOvoSadomasochismAttr> getList() {
        return repository.findAll();
    }
}
