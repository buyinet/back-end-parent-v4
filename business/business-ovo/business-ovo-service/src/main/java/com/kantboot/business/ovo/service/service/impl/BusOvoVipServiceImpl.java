package com.kantboot.business.ovo.service.service.impl;

import com.kantboot.business.ovo.module.entity.BusOvoVip;
import com.kantboot.business.ovo.service.repository.BusOvoVipRepository;
import com.kantboot.business.ovo.service.service.IBusOvoVipService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusOvoVipServiceImpl
implements IBusOvoVipService
{

    @Resource
    private BusOvoVipRepository repository;

    @Override
    public List<BusOvoVip> getAll() {
        return repository.findAll();
    }
}
