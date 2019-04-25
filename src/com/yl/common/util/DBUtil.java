package com.yl.common.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yl.common.dao.PublicDao;

@Service("dbUtil")
public class DBUtil {

	public static String getMaxaccept(PublicDao publicDao){
		return publicDao.getMaxaccept();
	}
	
	public static void insertSMS(PublicDao publicDao, String sendOpr, String phone, String message, String patientID, String code, String maxaccept){
		//插入信息发送表
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("maxaccept", publicDao.getMaxaccept());
		param.put("sendOpr", sendOpr);
		param.put("phone", phone);
		param.put("message", message);
		param.put("patientID", patientID);
		publicDao.insertSMS(param);
		
		//插入验证码记录表
		if(StringUtils.isNotBlank(code)){
			param.put("maxaccept", publicDao.getMaxaccept());
			param.put("code", code);
			param.put("orderID", maxaccept);
			publicDao.insertCodeNum(param);
		}
		
	}
}
