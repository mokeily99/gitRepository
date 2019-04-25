package com.yl.common.util;

import java.util.List;
import java.util.Map;

public class RandomUtil {

	/**
	 * list随机获取某个对象
	 * @param list
	 * @return
	 */
	public static Map<String, String> getRandomObj(List<Map<String, String>> list){
		if(list != null && list.size() > 0){
			int random = (int)(Math.random()*(list.size()));
			return list.get(random);
		}else{
			return null;
		}
	}
	
	public static void main(String[] args) {
		getRandomObj(null);
	}
}
