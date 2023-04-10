package com.kantboot.system.service.impl;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.kantboot.system.service.IRsaService;
import com.kantboot.system.service.ISysExceptionService;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * RSA加密解密服务实现类
 *
 * @author 方某方
 */
@Service
public class RsaServiceImpl implements IRsaService {


    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ISysExceptionService exceptionService;

    /**
     * 获取redis中privateKey的key
     *
     * @param publicKey 对应privateKey的publicKey
     * @return key
     */
    public String getPrivateKeyKey(String publicKey) {
        return "rsa:publicKey:" + publicKey + ":privateKey";
    }

    /**
     * 生成公钥
     * 生成公钥后，私钥会以`rsa:publicKey:{publicKey}:privateKey`为key存储在redis中
     *
     * @return 公钥
     */
    @Override
    public String createPublicKey() {
        // 生成公钥私钥
        RSA rsa = new RSA();
        String privateKey = rsa.getPrivateKeyBase64();
        String publicKey = rsa.getPublicKeyBase64();
        // 将对应的privateKey存放到redis数据库三十分钟
        redisUtil.setEx(getPrivateKeyKey(publicKey), privateKey, 30, TimeUnit.MINUTES);
        return publicKey;
    }

    /**
     * 获取私钥
     * 建议只在后端调用时使用，不要轻易暴露给前端
     *
     * @param publicKey 公钥
     * @return 私钥
     */
    @Override
    public String getPrivateKey(String publicKey) {
        // 从redis中获取对应的privateKey
        String privateKey = redisUtil.get(getPrivateKeyKey(publicKey));
        if (privateKey == null) {
            // 如果redis中没有对应的privateKey，则抛出异常
            throw exceptionService.getException("privateKeyNotExist");
        }
        return privateKey;
    }

    /**
     * 解密
     *
     * @param encryptStr 加密后的字符串
     * @param publicKey  公钥
     *                   用于获取私钥
     * @return 解密后的字符串
     */
    @Override
    public String decrypt(String encryptStr, String publicKey) {
        RSA rsa = new RSA(getPrivateKey(publicKey), publicKey);
        try {
            // 尝试解密
            String decryptStr = rsa.decryptStr(encryptStr, KeyType.PrivateKey);
            // 解密成功后，删除redis中的privateKey
            redisUtil.delete(getPrivateKeyKey(publicKey));
            return decryptStr;
        } catch (Exception e) {
            // 如果解密失败，则抛出异常
            throw exceptionService.getException("decryptionError");
        }

    }


}