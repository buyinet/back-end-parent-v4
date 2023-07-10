package com.kantboot.business.ovo.service.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.api.module.ApiPush;
import com.kantboot.api.service.IApiPushService;
import com.kantboot.business.ovo.module.entity.BusPushBind;
import com.kantboot.business.ovo.service.repository.BusPushBindRepository;
import com.kantboot.business.ovo.service.service.IBusPushBindService;
import com.kantboot.system.service.IEmailService;
import com.kantboot.system.service.ISysUserService;
import com.kantboot.util.common.exception.BaseException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Ovo用户推送表Service实现类
 *
 * @author 方某方
 */
@Slf4j
@Service
public class BusPushBindServiceImpl implements IBusPushBindService {

    @Resource
    private BusPushBindRepository repository;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private IApiPushService apiPushService;

    @Resource
    private IEmailService emailService;

    /**
     * cid绑定用户
     *
     * @param cid cid
     */
    @Override
    public void bind(String cid) {
        Long idOfSelf = null;
        try {
            idOfSelf = sysUserService.getIdOfSelf();
        } catch (BaseException e) {
            String notLogin = "notLogin";
            if (e.getStateCode().equals(notLogin)) {
                return;
            }
        }
        if (cid == null || idOfSelf == null) {
            return;
        }

        BusPushBind byCid = repository.findByCid(cid);
        if (byCid == null) {
            byCid = new BusPushBind();
        }
        byCid.setCid(cid).setUserId(idOfSelf);
        repository.save(byCid);
    }

    @Override
    public List<BusPushBind> getByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    /**
     * 根据用户id推送
     *
     * @param userId  用户id
     * @param apiPush 推送内容
     */
    @Override
    public void pushByUserId(Long userId, ApiPush apiPush) {
        List<BusPushBind> byUserId = repository.findByUserId(userId);

        ThreadUtil.execute(() -> {
            for (BusPushBind busPushBind : byUserId) {
                ApiPush apiPush1 = new ApiPush();
                BeanUtils.copyProperties(apiPush, apiPush1);
                apiPush1.setCid(busPushBind.getCid());
                apiPushService.push(apiPush1);
                log.info("推送内容：{}", apiPush1);
            }
        });
    }

    /**
     * 根据用户id推送uniPush和邮箱
     *
     * @param userId 用户id
     */
    @Override
    public void pushByUserIdWithEmail(Long userId, ApiPush apiPush) {

        List<BusPushBind> byUserId = repository.findByUserId(userId);

        ThreadUtil.execute(() -> {
            Boolean isOnline = false;
            for (BusPushBind busPushBind : byUserId) {
                ApiPush apiPush1 = new ApiPush();
                BeanUtils.copyProperties(apiPush, apiPush1);
                apiPush1.setCid(busPushBind.getCid());
                JSONObject push = apiPushService.push(apiPush1);

                String s = push.toJSONString();
                if (s.indexOf("successed_online") != -1) {
                    isOnline = true;
                }

                log.info("推送内容：{}", apiPush1);
            }
            // 如果在线
            if (isOnline) {
                return;
            }

            MailAccount mailAccount = emailService.getMailAccount();

            String email = sysUserService.getWithoutHideSensitiveInfoById(userId).getEmail();
            if (email == null || "".equals(email)) {
                return;
            }

            MailUtil.send(
                    mailAccount,
                    sysUserService.getWithoutHideSensitiveInfoById(userId).getEmail(),
                    apiPush.getTitle(),
                    apiPush.getContent(),
                    true);
        });

    }


}
