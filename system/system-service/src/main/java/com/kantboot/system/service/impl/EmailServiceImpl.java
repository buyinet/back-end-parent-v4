package com.kantboot.system.service.impl;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.kantboot.system.service.IEmailService;
import com.kantboot.system.service.ISysSettingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 邮件服务的实现类
 * @author
 */
@Service
public class EmailServiceImpl
implements IEmailService
{

    @Resource
    private ISysSettingService settingService;

    @Override
    public MailAccount getMailAccount() {
        Map<String, String> smtp = settingService.getMap("smtp");

        MailAccount account = new MailAccount();
        account.setHost(smtp.get("host"));
        account.setPort(25);
        account.setAuth(true);
        account.setFrom(smtp.get("email"));
        account.setUser(smtp.get("username"));
        account.setPass(smtp.get("password"));
        return account;
    }
}
