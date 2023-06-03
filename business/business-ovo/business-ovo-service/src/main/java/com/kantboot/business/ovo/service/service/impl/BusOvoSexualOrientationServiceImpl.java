package com.kantboot.business.ovo.service.service.impl;

import com.kantboot.business.ovo.module.entity.BusOvoSexualOrientation;
import com.kantboot.business.ovo.service.repository.BusOvoSexualOrientationRepository;
import com.kantboot.business.ovo.service.service.IBusOvoSexualOrientationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 性取向的服务接口实现类
 * @author 方某方
 */
@Service
public class BusOvoSexualOrientationServiceImpl implements IBusOvoSexualOrientationService {

    @Resource
    private BusOvoSexualOrientationRepository repository;

    /**
     * 获取所有的性取向
     * @return 性取向
     */
    @Override
    public List<BusOvoSexualOrientation> getAllSexualOrientation() {
        return repository.findAll();
    }

}
