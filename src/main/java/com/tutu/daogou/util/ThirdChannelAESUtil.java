package com.tutu.daogou.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class ThirdChannelAESUtil {

    public static final String HYD_PASSWORD = "huyidaiAes20181219";
    public static final String HYD_SECRET_KEY = "0123456789abcdef";

    public static final String MLL_PASSWORD = "mllAes20190219";
    public static final String MLL_SECRET_KEY = "0123456789abcdef";

    private static Logger logger = LoggerFactory.getLogger(ThirdChannelAESUtil.class);
    private static KeyGenerator kgen;
    private static SecretKeySpec key;
    private static Cipher cipher;

    private static ThirdChannelAESUtil hydAesSingle = new ThirdChannelAESUtil(HYD_PASSWORD, HYD_SECRET_KEY);
    private static ThirdChannelAESUtil mllAesSingle = new ThirdChannelAESUtil(MLL_PASSWORD, MLL_SECRET_KEY);

    public static ThirdChannelAESUtil getHydAesSingle(){
        return hydAesSingle;
    }

    public static ThirdChannelAESUtil getMllAesSingle(){
        return mllAesSingle;
    }

    private ThirdChannelAESUtil(String password, String secretKey) {
        try {
            kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            key = new SecretKeySpec(secretKey.getBytes(), "AES");
            cipher = Cipher.getInstance("AES");// 创建密码器
        } catch (Exception e) {
            logger.error("创建密码器出错", e);
        }
    }

    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @return
     */
    public static byte[] encrypt(String content) throws Exception {
        byte[] byteContent = content.getBytes("utf-8");
        cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
        byte[] result = cipher.doFinal(byteContent);
        return result; // 加密
    }

    /**
     * 解密
     *
     * @param content 待解密内容
     * @return
     */
    public static byte[] decrypt(byte[] content) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
        byte[] result = cipher.doFinal(content);
        return result; // 加密
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static void main(String[] args) {
        try {
//			 String content =
//			 "[{\"create_time\":1526470701420,\"event_code\":\"weixin_setting\",\"event_type\":\"page_switch\",\"id\":\"6a81208d-2341-4b32-99f3-4d282e99519b\",\"task_token\":\"910e5067a941450199067ba112c25cc1\"}]";
            String content = "";
            // 加密
            System.out.println("加密前：" + content);
            ThirdChannelAESUtil hydAESUtil = new ThirdChannelAESUtil(HYD_PASSWORD, HYD_SECRET_KEY);
            byte[] encryptResult = hydAESUtil.encrypt(content);
            String encryptResultStr = hydAESUtil.parseByte2HexStr(encryptResult);
            System.out.println("加密后：" + encryptResultStr);
            String dcryptResultStr = encryptResultStr;
            // 解密
            byte[] decryptFrom = hydAESUtil.parseHexStr2Byte(dcryptResultStr);
            byte[] decryptResult = hydAESUtil.decrypt(decryptFrom);
            System.out.println("解密后：" + new String(decryptResult));
        } catch (Exception ex) {

        }
    }

}
