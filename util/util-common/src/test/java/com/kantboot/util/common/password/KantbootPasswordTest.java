package com.kantboot.util.common.password;

import org.junit.jupiter.api.Test;

public class KantbootPasswordTest {

    /**
     * 密码加密
     */
    @Test
    void encode() {
        KantbootPassword kantbootPassword = new KantbootPassword();

        // 每次加密的结果都不一样
        // 加密后的密码：kantbootPassword.encode = 2e674d9859894505bb671edabebfdad3.6f287f1dc370d296d63d8f2be67e311f
        System.out.println("kantbootPassword.encode = " + kantbootPassword.encode("123456"));
    }

    /**
     * 密码比较
     */
    @Test
    void matches() {
        KantbootPassword kantbootPassword = new KantbootPassword();

        // 比较成功，返回结果：密码比较 = true
        System.out.println("密码比较 = " + kantbootPassword.matches("123456",
                "2e674d9859894505bb671edabebfdad3.6f287f1dc370d296d63d8f2be67e311f"));

        // 比较失败，返回结果：密码比较 = false
        System.out.println("密码比较 = " + kantbootPassword.matches("123456",
                "2e674d9859894505bb671edabebfdad3.6f287f1dc370d296d63d8f2be67e3112"));
    }



}
