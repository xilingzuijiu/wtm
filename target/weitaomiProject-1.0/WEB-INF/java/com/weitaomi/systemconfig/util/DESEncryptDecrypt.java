package com.weitaomi.systemconfig.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

public class DESEncryptDecrypt {

    private static Logger log = LoggerFactory.getLogger(DESEncryptDecrypt.class);

    private static final byte[] GET_KEY = { (byte) 0x15, (byte) 0xE7,
            (byte) 0xA1, (byte) 0x22, (byte) 0x96, (byte) 0x8B, (byte) 0x24,
            (byte) 0xFA };// 设置密钥，略去

    private static final byte[] GET_IV = { (byte) 0xCE, (byte) 0x35, (byte) 0x5,
            (byte) 0xD, (byte) 0x98, (byte) 0x91, (byte) 0x8, (byte) 0xA };// 设置向量，略去



    private String keyNum = "%&hj7x89H$yuBI0456FtmaT5&fvHUFCy76*h%(HilJ$lhj!y6&(*jkP87jH7&*^$^&*%&*%$@#@^";
    @SuppressWarnings("unused")
    private String ivNum = "E4ghj*Ghg7!rNIfb&95GUY86GfghUb#er57HBh(u%g6HJ($jhWk7&!hg4ui%$hjk";

    private AlgorithmParameterSpec iv = null;// 加密算法的参数接口，IvParameterSpec是它的一个实现
    private Key key = null;

    /**
     * 声明顺序
     * @throws Exception
     */
    public DESEncryptDecrypt() throws Exception {
        iv = new IvParameterSpec(getLegalIV());// 设置向量
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
        DESKeySpec keySpec = new DESKeySpec(getLegalKey());// 设置密钥参数
        key = keyFactory.generateSecret(keySpec);// 得到密钥对象
    }

    /**
     * 获得密钥
     * @return
     */
    private byte[] getLegalKey() {
        StringBuffer sTemp = new StringBuffer(this.keyNum);
        byte[] bytTemp = GET_KEY;
        int keyLength = bytTemp.length;
        if (sTemp.length() > keyLength) {
            sTemp = new StringBuffer(sTemp.substring(0, keyLength));
        } else {
            if (sTemp.length() < keyLength) {

                for (int i = 0; i < keyLength - sTemp.length(); i++) {
                    sTemp.append(' ');
                }

            }
        }
        return sTemp.toString().getBytes();
    }

    /**
     * 获得初始向量I
     * @return
     */
    private byte[] getLegalIV() {
        StringBuffer sTemp = new StringBuffer(this.keyNum);
        byte[] bytTemp = GET_IV;
        int ivLength = bytTemp.length;
        if (sTemp.length() > ivLength) {
            sTemp = new StringBuffer(sTemp.substring(0, ivLength));
        } else {
            if (sTemp.length() < ivLength) {
                for (int i = 0; i < ivLength - sTemp.length(); i++) {
                    sTemp.append(' ');
                }
            }
        }
        return sTemp.toString().getBytes();
    }


    /**
     * 加密
     * @param data
     * @return
     * @throws Exception
     */
    public String encode(String data) throws Exception {
        Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");// 得到加密对象Cipher
        enCipher.init(Cipher.ENCRYPT_MODE, key, iv);// 设置工作模式为加密模式，给出密钥和向量
        byte[] pasByte = enCipher.doFinal(data.getBytes("utf-8"));
        sun.misc.BASE64Encoder base64Encoder = new sun.misc.BASE64Encoder();
        return base64Encoder.encode(pasByte);
    }

    /**
     * 解密
     * @param data
     * @return
     * @throws Exception
     */
    public String decode(String data) throws Exception {
        Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        deCipher.init(Cipher.DECRYPT_MODE, key, iv);
        sun.misc.BASE64Decoder  base64Decoder = new sun.misc.BASE64Decoder();
        byte[] pasByte = deCipher.doFinal(base64Decoder.decodeBuffer(data));
        return new String(pasByte, "UTF-8");
    }

    public String urlEncode(String encodeStr) {
        String tmpStr = encodeStr;
        try {
            tmpStr = encode(tmpStr);
            tmpStr = java.net.URLEncoder.encode(tmpStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            log.error("urlencode 转码错误:", e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
        return tmpStr;
    }

}