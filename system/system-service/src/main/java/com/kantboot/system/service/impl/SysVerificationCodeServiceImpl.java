package com.kantboot.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.kantboot.system.module.entity.SysVerificationCode;
import com.kantboot.system.repository.SysVerificationCodeRepository;
import com.kantboot.system.service.IEmailService;
import com.kantboot.system.service.ISysVerificationCodeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 验证码的服务实现类
 * @author 方某方
 */
@Service
public class SysVerificationCodeServiceImpl implements
        ISysVerificationCodeService
{

    @Resource
    private SysVerificationCodeRepository repository;

    @Resource
    private IEmailService emailService;

    @Override
    public String generateVerificationCode() {
        // 随机验证码，最低000000，最高999999
        int random = (int) (Math.random() * 1000000);
        String code = String.format("%06d", random);
        return code;
    }


    @Override
    public String sendVerificationCode(String sendTo, String typeCode, String actionCode) {
        SysVerificationCode sysVerificationCode = new SysVerificationCode();
        sysVerificationCode.setSendTo(sendTo);
        sysVerificationCode.setTypeCode(typeCode);
        sysVerificationCode.setActionCode(actionCode);

        // 生成验证码
        sysVerificationCode.setVerificationCode(generateVerificationCode());

        // 有效期5分钟
        sysVerificationCode.setGmtExpire(
                new java.util.Date(System.currentTimeMillis() + 5 * 60 * 1000)
        );

        // 发送验证码
        sendEmail(sysVerificationCode.getVerificationCode(), sendTo, actionCode);

        repository.save(sysVerificationCode);
        return sysVerificationCode.getVerificationCode();
    }

    /**
     * 邮箱验证码的内容
     * @param verificationCode 验证码
     * @param sendTo 发送给谁
     * @param actionCode 验证码的用途
     * @return 邮箱验证码的内容
     */
    private void sendEmail(String verificationCode, String sendTo, String actionCode) {

        MailAccount account = emailService.getMailAccount();

        String title = "";
        if(actionCode.equals("bindEmail")){
            title = "绑定邮箱";
        }else{
            title = "验证码";
        }

        final String finalTitle = title;

        ThreadUtil.execute(() -> {

            MailUtil.send(account,
                CollUtil.newArrayList(sendTo),
                finalTitle,
                """
                        <html>
                            <head>

                                <title>Home</title>
                                <!-- utf-8 -->
                                <meta charset="utf-8">
                            </head>
                            <body
                            style="margin: 0; padding: 2vw;"
                            >
                                <div class="bg"></div>
                                <div class="container">
                                    <div class="row">
                                        <div
                                        class="title"
                                        >您的验证码</div>
                                        <div
                                        class="ver-code">
                                        """ + verificationCode + """
                                        </div>
                                        <div
                                        class="expire"
                                        >5分钟内有效</div>
                                    </div>
                                </div>
                            </body>

                            <style>
                                .bg {
                                    position: fixed;
                                    width: 100%;
                                    height: 100%;
                                    background-color: #fff;
                                    opacity: .5;
                                    z-index: -1;
                                    top:0;
                                    left:0;
                                }

                                .container {
                                    position: relative;
                                    width: 80%;
                                    background-color: #fff;
                                    padding: 2vw;
                                    /* 手机宽高 */
                                    max-width: 750px;
                                    margin: 0 auto;
                                    margin-top: 10vw;
                                    border-radius: 2vw;
                                    border: .5w dashed #333;

                                    box-sizing: border-box;
                                }

                                .row {
                                    text-align: center;
                                    border: .5 dashed #333;
                                    /* padding是.container宽度的百分之二十 */
                                    padding: 2vw;

                                    box-sizing: border-box;
                                    border-radius: 2vw;
                                    background-color: #fff;
                                }

                                .title {
                                    font-size: 3vw;
                                    color: #333;
                                }

                                .ver-code {
                                    font-size: 8vw;
                                    font-weight: bold;
                                    color: #333;
                                }
                                
                                .expire {
                                    font-size: 3vw;
                                    color: #333;
                                }
                                
                               \s
                            </style>
                        </html>
                        """,
                true);
        });
    }

}
