package com.weitaomi.systemconfig.util;

import org.springframework.util.StringUtils;

/**
 * Created by Martin on 2016/1/31.
 */
public class DESUtil {
    /**
     * @Fields desTool : 加密解密的工具类
     */
    private static DESEncryptDecrypt desTool;

    /**
     * @Title: getInstance
     * @Description: 获取加密解密工具类的方法
     * @return
     * @throws Exception
     * @throws
     */
    private static DESEncryptDecrypt getInstance() throws Exception{
        if(desTool == null){
            desTool = new DESEncryptDecrypt();
        }
        return desTool;
    }
    /**
     * @Title: decode
     * @Description: 解密算法
     * @param secret
     * @return
     * @throws Exception
     * @throws
     */
    public static String decode(String secret) throws Exception{
        if(StringUtils.isEmpty(secret)){
            return secret;
        }
        return getInstance().decode(secret);
    }

    /**
     * @Title: encode
     * @Description: 解密算法
     * @param source
     * @return
     * @throws Exception
     * @throws
     */
    public static String encode(String source) throws Exception{
        if(StringUtil.isEmpty(source)){
            return source;
        }
        return getInstance().encode(source);
    }
}