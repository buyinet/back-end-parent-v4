package com.kantboot.business.ovo.service.service.impl;

import com.kantboot.business.ovo.module.entity.BusPush;
import com.kantboot.business.ovo.service.repository.BusPushRepository;
import com.kantboot.business.ovo.service.service.IBusPushService;
import com.kantboot.system.service.ISysUserService;
import com.kantboot.util.common.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Ovo用户推送表Service实现类
 * @author 方某方
 */
@Service
public class BusPushServiceImpl implements IBusPushService {

    @Resource
    private BusPushRepository repository;

    @Resource
    private ISysUserService sysUserService;

    /**
     * cid绑定用户
     * @param cid cid
     */
    @Override
    public void bind(String cid) {
        Long idOfSelf = null;
        try {
            idOfSelf = sysUserService.getIdOfSelf();
        } catch (BaseException e) {
            String notLogin="notLogin";
            if (e.getStateCode().equals(notLogin)){
                return;
            }
        }
        if (cid == null||idOfSelf==null){
            return;
        }

        BusPush byCid = repository.findByCid(cid);
        if (byCid == null){
            byCid = new BusPush();
        }
        byCid.setCid(cid).setUserId(idOfSelf);
        repository.save(byCid);
    }

    @Override
    public List<BusPush> getByUserId(Long userId) {
        return repository.findByUserId(userId);
    }
}
