package com.yl.common.util;

import java.util.ResourceBundle;

public class ConfigUtil {

	/**
	 * 根据KEY获取CONFIG配置文件VALUE
	 * @param key
	 * @return
	 */
	public static String getConfigKey(String key){
		ResourceBundle resource = ResourceBundle.getBundle("CONFIG");
	    String value = resource.getString(key);  
	    return value;
	}
}
