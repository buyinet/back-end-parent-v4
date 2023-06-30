package com.kantboot.api.util.wechat.pay;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class WeChatNotificationDecryptor {
    private static final String ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;

    public static String decryptNotification(String encryptedData, String key, String nonce, String associatedData) {
        try {
            // 将密钥和随机串进行Base64解码
            byte[] decodedKey = Base64.getDecoder().decode(key);
            byte[] decodedNonce = Base64.getDecoder().decode(nonce);

            // 创建AES密钥对象和GCM参数规范
            SecretKeySpec secretKeySpec = new SecretKeySpec(decodedKey, ALGORITHM);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, decodedNonce);

            // 创建解密器
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);

            // 检查associatedData是否为null，如果为null则使用空字符串作为默认值
            if (associatedData == null) {
                associatedData = "";
            }
            cipher.updateAAD(associatedData.getBytes(StandardCharsets.UTF_8));

            // 解密数据密文
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // 将解密后的字节数组转换为字符串
            String decryptedData = new String(decryptedBytes, StandardCharsets.UTF_8);

            return decryptedData;
        } catch (Exception e) {
            // 处理解密异常
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String ciphertext = "1mcNyYvggPVxaQGIJGOTw8XRhm7LzG9PL5wC1PiFkAKNNLolq/mFEsXMzEnbrBIf3znJ9h/qV0v5IpHnMv/pZWPrmS1A5xpMUsqk6JHxjlViJX8wWHVLXkL13r3lo04mWPcCN46/y/Xrc05c8rtZ0okBVoE/yjQW8Ty3zeZ427YTFrM2Spn1UgD1Gc+3uyrFWoj0PKqNpJRbeNho6Uy9GPhyejnbNuao3OS4sUZYrS0zLiHvmF5dtR/rez1kQyFgP08vfgMZI+6tvr7tRCJ7sTizcG4Mw4JPBdC2ku9d1NbhJuy9IUeuE4snLQiiCrc2OS5eOecpuzdHUb7LQVRssgSQHsGaReTTmsweK1PhXdVnx4t09idDxfr6L/ddaniQRL31g7wY4ybO+PvybE75gUjbznpkS1+Gd1eParkhcytiZEy7m8FTFFqjK/9lCae35gPRigqXHIswpcf+0X5KOiX17ajsD6lWkRtx78sQsRFM7NuDzJpqyXV6x1gREln9riYb541xcptsrHk0jS7imbGfjv/BRaFNGwnv8sVZfAh+djTZliO6lECB7pXsWQCCrnDfDg=="; // 替换为实际的密文
        String key = "Wsfzy123123123123123123123123123"; // 替换为实际的密钥
        String iv = "CjxK82NXbsMJ"; // 替换为实际的IV

        byte[] decryptedData = decrypt(ciphertext, key, iv);
        String plaintext = new String(decryptedData, StandardCharsets.UTF_8);

        System.out.println("Decrypted plaintext: " + plaintext);
    }

    public static byte[] decrypt(String ciphertext, String key, String iv) throws Exception {
        byte[] ciphertextBytes = Base64.getDecoder().decode(ciphertext);
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);

        // 创建密钥规范
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

        // 创建GCM参数规范
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, ivBytes);

        // 创建AES-GCM解密密码器
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

        // 解密密文
        byte[] decryptedBytes = cipher.doFinal(ciphertextBytes);

        return decryptedBytes;
    }
}