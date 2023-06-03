package com.kantboot.business.ovo.service.service.impl;

import com.kantboot.business.ovo.module.dto.BusOvoUserBindDto;
import com.kantboot.business.ovo.module.entity.BusOvoEmotionalOrientation;
import com.kantboot.business.ovo.module.entity.BusOvoUserBind;
import com.kantboot.business.ovo.service.repository.BusOvoUserBindRepository;
import com.kantboot.business.ovo.service.service.IBusOvoUserBindService;
import com.kantboot.system.module.entity.SysUser;
import com.kantboot.system.repository.SysUserRepository;
import com.kantboot.system.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 绑定的用户的服务实现类
 * @author 方某方
 */
@Service
public class BusOvoUserBindServiceImpl implements IBusOvoUserBindService {

    @Resource
    private BusOvoUserBindRepository repository;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private SysUserRepository sysUserRepository;

    @Override
    public BusOvoUserBind bind(BusOvoUserBindDto dto) {
        // 获取当前用户
        SysUser sysUser = sysUserService.getWithoutHideSensitiveInfo();
        sysUser.setFileIdOfAvatar(dto.getFileIdOfAvatar())
                .setNickname(dto.getNickname())
                .setGender(dto.getGender())
                .setBirthday(dto.getBirthday());
        sysUserRepository.save(sysUser);

        // 获取当前用户的id
        Long userId = sysUser.getId();
        // 根据用户id查询用户绑定信息
        BusOvoUserBind busOvoUserBind = repository.findByUserId(userId);
        // 如果用户绑定信息为空
        if (busOvoUserBind == null) {
            // 创建用户绑定信息
            busOvoUserBind = new BusOvoUserBind();
            // 设置用户id
            busOvoUserBind.setUserId(userId);
            repository.save(busOvoUserBind);
        }
        busOvoUserBind
                .setSexualOrientationCode(dto.getSexualOrientationCode())
                .setSadomasochismAttrCode(dto.getSadomasochismAttrCode());

        List<BusOvoEmotionalOrientation> busOvoEmotionalOrientationList = new ArrayList<>();
        List<String> emotionalOrientationCodeList = dto.getEmotionalOrientationCodeList();
        for (String emotionalOrientationCode : emotionalOrientationCodeList) {
            busOvoEmotionalOrientationList.add(new BusOvoEmotionalOrientation().setCode(emotionalOrientationCode));
        }

        busOvoUserBind.setEmotionalOrientationList(busOvoEmotionalOrientationList);

        BusOvoUserBind result = repository.save(busOvoUserBind);
        return result;
    }


    @Override
    public BusOvoUserBind getSelf() {
        SysUser sysUser = sysUserService.getWithoutHideSensitiveInfo();
        Long userId = sysUser.getId();
        BusOvoUserBind busOvoUserBind = repository.findByUserId(userId);
        return busOvoUserBind;
    }

}



