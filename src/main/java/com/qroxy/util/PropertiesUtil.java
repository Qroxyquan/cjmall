package com.qroxy.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 *@desc:用于读取mmall配置文件信息
 *
 *@author:Qroxy
 *
 *@date:2018/10/7 7:02 PM
 *
 *@param:
 *
 *@type:
 *
 */

public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties props;
//静态块优于普通代码块，普通代码块优于构造器代码块！静态代码块在系统启动时便会启动
    static {
        //静态代码块，类加载器启动时执行一次，一般用于初始化静态变量
        String fileName = "mmall.properties";
        props = new Properties();
        try {
            //普通代码块
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));
        } catch (IOException e) {
            logger.error("配置文件读取异常",e);
        }
    }

    public static String getProperty(String key){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;
        }
        return value.trim();
    }

    public static String getProperty(String key, String defaultValue){

        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            value = defaultValue;
        }
        return value.trim();
    }



}
