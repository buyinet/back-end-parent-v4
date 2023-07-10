package com.kantboot.system.service;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;

/**
 * 邮件服务
 * @author 方某方
 */
public interface IEmailService {

    /**
     * 邮箱账户
     * @return 邮箱账户
     */
    MailAccount getMailAccount();

}
