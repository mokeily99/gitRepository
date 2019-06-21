package com.yl.common.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

public class HttpUtil {
	public static Logger logger = Logger.getLogger("com.yl.common.util.HttpUtil");
	/**
	 * 发送HTTP请求
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String parameterData) throws Exception {
		// String parameterData =
		// "cdkey=7SDK-LHW-0588-RBRUM&password=352064&phone=18686530251&message=欢迎使用同鑫热力投诉系统，您的短信验证码是0000，请确保由您本人完成操作。如非本人操作，请忽略本短信。【同鑫热力】&seqid=123&addserial=";

		logger.info("请求参数：" + parameterData);
		URL localURL = new URL("http://sdk.univetro.com.cn:6200/sdkproxy/sendsms.action");
		URLConnection connection = localURL.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

		httpURLConnection.setDoOutput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
		httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		httpURLConnection.setRequestProperty("Content-Length", String.valueOf(parameterData.length()));

		OutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;

		try {
			outputStream = httpURLConnection.getOutputStream();
			outputStreamWriter = new OutputStreamWriter(outputStream, "utf-8");

			outputStreamWriter.write(parameterData.toString());
			outputStreamWriter.flush();

			if (httpURLConnection.getResponseCode() >= 300) {
				throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
			}

			inputStream = httpURLConnection.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
			reader = new BufferedReader(inputStreamReader);

			while ((tempLine = reader.readLine()) != null) {
				resultBuffer.append(tempLine);
			}
		} finally {
			if (outputStreamWriter != null) {
				outputStreamWriter.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
			if (reader != null) {
				reader.close();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}

		logger.info("应答参数：" + resultBuffer.toString());
		return resultBuffer.toString();
	}

	public static JSONObject doPostJson(String param, String url, String msg) throws UnsupportedEncodingException {
//		param = URLEncoder.encode(param, "UTF-8");
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		JSONObject jsStr = null;
		URLConnection conn = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			conn = realUrl.openConnection();
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
			logger.info(msg+"请求参数-REQUEST：" + param);
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			// 解析json对象
			if(result != null && !"".equals(result)){
				jsStr = JSONObject.fromObject(result);
				logger.info(msg+"返回参数-RESPONSE：" + jsStr);
			}else{
				jsStr =  JSONObject.fromObject("{\"RESULT_CODE\":\"9999\", \"RESULT_MSG\":\"接口返回参数为空！\"}");
				logger.info(msg+"返回参数-RESPONSE：返回参数为空！" );
			}
		} catch (Exception e) {
			logger.error(msg+"失败：" + e);
			jsStr = JSONObject.fromObject("{\"RESULT_CODE\":\"9999\", \"RESULT_MSG\":\"系统异常！\"}");
		}finally{
			if(out != null){
				out.flush();
				out.close();
			}
		}
		
		return jsStr;
	}
	/**
	 * API登录
	 * @param username
	 * @param password
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static JSONObject login(String username, String password, String url) throws UnsupportedEncodingException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("username", username);
		param.put("password", MD5Utils.MD5Encode(password, "utf-8"));
		param.put("url", url);
		JSONObject resp = doPostJson(JsonUtils.toJsonObj(param), ConfigUtil.getConfigKey("SWITCH_URL")+"/API/login", "API登录");
		return resp;
	}
	/**
	 * API心跳包
	 * @param token
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static JSONObject heartbeat(String token, String url) throws UnsupportedEncodingException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("url", url);
		JSONObject resp = doPostJson(JsonUtils.toJsonObj(param), ConfigUtil.getConfigKey("SWITCH_URL")+"/API/heartbeat?token=" + token, "API心跳包");
		return resp;
	}
	/**
	 * 登出API
	 * @param token
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static JSONObject logout(String token) throws UnsupportedEncodingException{
		Map<String, String> param = new HashMap<String, String>();
		JSONObject resp = doPostJson(JsonUtils.toJsonObj(param), ConfigUtil.getConfigKey("SWITCH_URL")+"/API/logout?token=" + token, "API登出");
		return resp;
	}
	/**
	 * 查询坐席状态
	 * @param queueid
	 * @param extid
	 * @param password
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static JSONObject agentQuery(String queueid, String extid, String password, String token) throws UnsupportedEncodingException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("queueid", queueid);
		param.put("extid", extid);
		param.put("password", password);
		JSONObject resp = doPostJson(JsonUtils.toJsonObj(param), ConfigUtil.getConfigKey("SWITCH_URL")+"/API/agent_query?token="+token, "查询坐席状态");
		return resp;
	}
	/**
	 * 坐席登入
	 * @param queueid
	 * @param extid
	 * @param password
	 * @param token
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static JSONObject agentLogin(String queueid, String extid, String password, String token) throws UnsupportedEncodingException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("queueid", queueid);
		param.put("extid", extid);
		param.put("password", password);
		JSONObject resp = doPostJson(JsonUtils.toJsonObj(param), ConfigUtil.getConfigKey("SWITCH_URL")+"/API/agent_login?token="+token, "坐席登入");
		return resp;
	}
	/**
	 * 坐席置忙
	 * @param queueid
	 * @param extid
	 * @param token
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static JSONObject agentBreak(String queueid, String extid, String token) throws UnsupportedEncodingException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("queueid", queueid);
		param.put("extid", extid);
		JSONObject resp = doPostJson(JsonUtils.toJsonObj(param), ConfigUtil.getConfigKey("SWITCH_URL")+"/API/agent_break?token="+token, "坐席置忙");
		return resp;
	}
	/**
	 * 坐席置闲
	 * @param queueid
	 * @param extid
	 * @param token
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static JSONObject agentResume(String queueid, String extid, String token) throws UnsupportedEncodingException{
		Map<String, String> param = new HashMap<String, String>();
		param.put("queueid", queueid);
		param.put("extid", extid);
		JSONObject resp = doPostJson(JsonUtils.toJsonObj(param), ConfigUtil.getConfigKey("SWITCH_URL")+"/API/agent_resume?token="+token, "坐席置闲");
		return resp;
	}
	public static void main(String[] args) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("xmlhead", "<?xml version='1.0' encoding='UTF-8'?><InterBOSS><Version>0100</Version><TestFlag>0</TestFlag><BIPType><BIPCode>BIP5A031</BIPCode><ActivityCode>T5000031</ActivityCode><ActionCode>0</ActionCode></BIPType><RoutingInfo><OrigDomain>UPMS</OrigDomain><RouteType>01</RouteType><Routing><HomeDomain>BOSS</HomeDomain><RouteValue>13844417949</RouteValue></Routing></RoutingInfo><TransInfo><SessionID>UPMSPID20150906151440685764</SessionID><TransIDO>TransIDO20150906151440685764</TransIDO><TransIDOTime>20150906151440</TransIDOTime></TransInfo><SNReserve><TransIDC>9980770120150906151404374005468</TransIDC><ConvID>30b57e85-602f-4580-a916-4abb78e37704</ConvID><CutOffDay>20150906</CutOffDay><OSNTime>20150906151404</OSNTime><OSNDUNS>9980</OSNDUNS><HSNDUNS>4310</HSNDUNS><MsgSender>0056</MsgSender><MsgReceiver>4311</MsgReceiver><Priority>3</Priority><ServiceLevel>5</ServiceLevel></SNReserve></InterBOSS>");
		param.put("xmlbody", "<?xml version='1.0' encoding='UTF-8'?><InterBOSS><SvcCont><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?><AuthReq><OrgID>0006</OrgID><OrgName>web</OrgName><MobileNum>13844417949</MobileNum></AuthReq>]]></SvcCont></InterBOSS>");
		JSONObject jsonObject=JSONObject.fromObject(param);
		doPostJson(jsonObject.toString(), "http://10.162.200.95:8600/servlet/BossServ", "sss");
	}
}
