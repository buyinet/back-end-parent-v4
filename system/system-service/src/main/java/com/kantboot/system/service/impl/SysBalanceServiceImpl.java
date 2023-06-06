package com.kantboot.system.service.impl;

import com.kantboot.system.module.entity.SysBalance;
import com.kantboot.system.repository.SysBalanceRepository;
import com.kantboot.system.service.ISysBalanceService;
import com.kantboot.system.service.ISysBalanceTypeService;
import com.kantboot.system.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 余额服务接口实现类
 *
 * @author 方某方
 */
@Service
public class SysBalanceServiceImpl implements ISysBalanceService {

    @Resource
    private SysBalanceRepository repository;

    @Resource
    private ISysUserService userService;

    @Resource
    private ISysBalanceTypeService balanceTypeService;


    @Override
    public Map<String, Double> getSelfMap() {
        // 获取用户id
        Long userId = userService.getIdOfSelf();
        Map<String, Double> result = new HashMap<>(10);
        List<SysBalance> byUserId = repository.findByUserId(userId);
        for (SysBalance sysBalance : byUserId) {
            result.put(sysBalance.getBalanceTypeCode(), sysBalance.getBalance());
        }
        // 获取余额类型
        Map<String, String> map = balanceTypeService.getMap();
        Set<String> strings = map.keySet();
        for (String string : strings) {
            if (!result.containsKey(string)) {
                result.put(string, 0.0);
            }
        }

        return result;
    }
}
