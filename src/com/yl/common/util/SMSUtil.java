package com.yl.common.util;

public class SMSUtil {

	public static void sendSMS(String phone, String message) throws Exception{
		StringBuffer sendData = new StringBuffer();
		sendData.append("cdkey=" + ConfigUtil.getConfigKey("CD_KEY"));
		sendData.append("&password=" + ConfigUtil.getConfigKey("SMS_PWD"));
		sendData.append("&phone=" + phone);
		sendData.append("&message=" + message);
		sendData.append("&seqid=123");
		sendData.append("&addserial=");
		HttpUtil.doPost(sendData.toString());
	}
}
