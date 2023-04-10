package com.kantboot.system.service;


/**
 * RSA加密解密服务
 *
 * @author 方某方
 */
public interface IRsaService {

    /**
     * 生成公钥
     * 生成公钥后，私钥会以`rsa:publicKey:{publicKey}:privateKey`为key存储在redis中
     * @return 公钥
     */
    String createPublicKey();

    /**
     * 获取私钥
     * 建议只在后端调用时使用，不要轻易暴露给前端
     * @param publicKey 公钥
     * @return 私钥
     */
    String getPrivateKey(String publicKey);

    /**
     * 解密
     * @param encryptStr 加密后的字符串
     * @param publicKey 公钥
     *                  用于获取私钥
     * @return 解密后的字符串
     */
    String decrypt(String encryptStr,String publicKey);


}