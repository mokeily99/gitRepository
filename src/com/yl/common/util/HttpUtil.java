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
import java.net.URLEncoder;

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

	public static JSONObject doPostJson(String param, String url) throws UnsupportedEncodingException {
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
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
			logger.info("用户资料查询请求参数-REQUEST：" + param);
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
				logger.info("用户资料查询返回参数-RESPONSE：" + jsStr);
			}else{
				jsStr =  JSONObject.fromObject("{\"RESULT_CODE\":\"9999\", \"RESULT_MSG\":\"接口返回参数为空！\"}");
				logger.info("用户资料查询返回参数-RESPONSE：返回参数为空！" );
			}
		} catch (Exception e) {
			logger.error("用户资料查询失败：" + e);
			jsStr = JSONObject.fromObject("{\"RESULT_CODE\":\"9999\", \"RESULT_MSG\":\"系统异常！\"}");
		}finally{
			if(out != null){
				out.flush();
				out.close();
			}
		}
		
		return jsStr;
	}

	public static void main(String[] args) throws Exception {
		String parameterData = "cdkey=7SDK-LHW-0588-RBRUM&password=352064&phone=18686530251&message=欢迎使用同鑫热力投诉系统，您的短信验证码是0000，请确保由您本人完成操作。如非本人操作，请忽略本短信。【同鑫热力】&seqid=123&addserial=";
		System.out.println(doPost(parameterData));
	}
}
