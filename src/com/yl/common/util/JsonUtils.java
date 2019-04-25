package com.yl.common.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtils {

	public static String toJsonArr(Object obj){
		JSONArray jarr = JSONArray.fromObject(obj);
		return jarr.toString();
	}
	
	public static String toJsonObj(Object obj){
		JSONObject jo = JSONObject.fromObject(obj);
		return jo.toString();
	}
}
