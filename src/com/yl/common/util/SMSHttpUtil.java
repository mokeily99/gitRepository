package com.yl.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * http请求工具
 */
public class SMSHttpUtil {
    /**
     * 构造通用参数timestamp、sig和respDataType
     * 
     * @return
     */
    public static String createCommonParam(String sid,String token) {
        // 时间戳
        long timestamp = System.currentTimeMillis();
        // 签名
        String sig = DigestUtils.md5Hex(sid + token + timestamp);

        return "&timestamp=" + timestamp + "&sig=" + sig + "&respDataType=" + Config.RESP_DATA_TYPE;
    }

    /**
     * post请求
     * 
     * @param url
     *            功能和操作
     * @param body
     *            要post的数据
     * @return
     * @throws IOException
     */
    public static String post(String url, String body) {
        System.out.println("发送短信参数:" + body);

        String result = "";
        try {
            OutputStreamWriter out = null;
            BufferedReader in = null;
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();

            // 设置连接参数
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(20000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 提交数据
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(body);
            out.flush();

            // 读取返回数据
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = "";
            boolean firstLine = true; // 读第一行不加换行符
            while ((line = in.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                } else {
                    result += System.getProperty("line.separator");
                }
                result += line;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("发送短信返回参数:" + result);
        
        return result;
    }

    /**
     * 回调测试工具方法
     * 
     * @param url
     * @return
     */
    public static String postHuiDiao(String url, String body) {
        String result = "";
        try {
            OutputStreamWriter out = null;
            BufferedReader in = null;
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();

            // 设置连接参数
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(20000);

            // 提交数据
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(body);
            out.flush();

            // 读取返回数据
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = "";
            boolean firstLine = true; // 读第一行不加换行符
            while ((line = in.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                } else {
                    result += System.getProperty("line.separator");
                }
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public String sendSMS(String phone, String smsContent) throws UnsupportedEncodingException{
    	StringBuilder sb = new StringBuilder();
		sb.append("accountSid").append("=").append(Config.ACCOUNT_SID);
		sb.append("&to").append("=").append(phone);
		sb.append("&param").append("=").append(URLEncoder.encode("","UTF-8"));
		//sb.append("&templateid").append("=").append("1251");
		sb.append("&smsContent").append("=").append( URLEncoder.encode(smsContent,"UTF-8"));
		String body = sb.toString() + SMSHttpUtil.createCommonParam(Config.ACCOUNT_SID, Config.AUTH_TOKEN);
		String result = SMSHttpUtil.post(Config.BASE_URL, body);
		return result;
    }
    
    public static void main(String[] args) {
    	SMSHttpUtil am = new SMSHttpUtil();
		try {
			am.sendSMS("18088645703", "【弘昇服务】生日快乐！");//【弘昇服务】您的验证码为1111，该验证码五分钟内有效，请勿泄露他人。
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}