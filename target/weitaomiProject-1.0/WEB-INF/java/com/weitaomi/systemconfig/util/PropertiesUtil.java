package com.weitaomi.systemconfig.util;


import com.weitaomi.systemconfig.exception.BusinessException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by supumall on 2015/6/25.
 */
public class PropertiesUtil {
    public static Properties properties = new Properties();
    public static List<String> configFile = Arrays.asList(
            "server.properties","properties/configuration.properties","properties/message.properties","properties/requestUrl.properties");
    static {
        try {
            for(String fileName:configFile) {
                InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
                properties.load(in);
            }
        } catch (IOException e) {
            throw new BusinessException("读取配置文件错误！");
        }
    }
    public static String getValue(String key){
        try {
            return new String(properties.getProperty(key,"").getBytes("ISO-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
